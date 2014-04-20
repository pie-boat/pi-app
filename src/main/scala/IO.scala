package pieboat.pi

import pieboat.network.Messages

object IO {
  if(Config.onPi) {
    com.pi4j.wiringpi.Gpio.wiringPiSetupGpio();
  }

  def setGPIOWith(s: String) = { println("Received: " + s); Messages.readAndExecute(
    ping = () => this.ping,
    status = () => "Not implemented",
    speed = x => this.speed(x),
    direction = x => this.direction(x),
    frontLights = on => this.frontLights(on),
    sideLights = on => this.sideLights(on),
    debugMA = x => this.MAspeed(x),
    debugMB = x => this.MBspeed(x),
    debugMC = x => this.MCspeed(x)
  )(s)}

  def ping = {
    Main.stateActor ! Ping
    Messages.answerHeartbeat
  }

  def speed(x: Integer) = {
    if(math.abs(x) <= 500) {
      State.speed = x
      this._setMotorsSpeed
      "OK"
    } else {
      "NOK"
    }
  }

  def direction(x: Integer) = {
    if(math.abs(x) <= 500) {
      State.direction = x
      this._setMotorsSpeed
      "OK"
    } else {
      "NOK"
    }
  }

  def _setMotorsSpeed {
    if(math.abs(State.speed) >= 300 && math.abs(State.direction) < 250) {
      this.MCspeed((math.abs(State.speed) - 300) * math.signum(State.speed) * (500/200))
    } else {
      this.MCspeed(0)
    }

    if(State.speed > 0) {
      if(State.direction != 0) {
        this.MAspeed((State.speed * ((State.direction + 500) / 500.0)).toInt)
        this.MBspeed((State.speed * math.abs((State.direction + 500) / 500.0 - 2)).toInt)
      } else {
        this.MAspeed(State.speed)
        this.MBspeed(State.speed)
      }
    } else {
      this.MAspeed(0)
      this.MBspeed(0)
    }
  }

  def frontLights(on: Boolean) = {
    if(on) {
      GpioPins.pin25.high
    } else {
      GpioPins.pin25.low
    }
    "OK"
  }

  def sideLights(on: Boolean) = {
    if(on) {
      GpioPins.pin24.high
    } else {
      GpioPins.pin24.low
    }
    "OK"
  }

  def MAspeed(x: Integer): String = {
    if(math.abs(x) <= 500) {
      GpioPins.pin22.low

      if (x >= 0) {
        GpioPins.pin23.high
      } else {
        GpioPins.pin23.low
      }

      GpioPins.pin27.pwm(math.abs(x))
    } else {
      MAspeed(500 * math.signum(x))
    }
    "OK"
  }

  def MBspeed(x: Integer): String = {
    if(math.abs(x) <= 500) {
      GpioPins.pin3.low

      if (x >= 0) {
        GpioPins.pin4.high
      } else {
        GpioPins.pin4.low
      }

      GpioPins.pin2.pwm(math.abs(x))
    } else {
      MBspeed(500 * math.signum(x))
    }
    "OK"
  }

  def MCspeed(x: Integer): String = {
    if(math.abs(x) <= 500) {
      if (x >= 0) {
        GpioPins.pin17.pwm(0)
        GpioPins.pin18.pwm(math.abs(x))
      } else {
        GpioPins.pin18.pwm(0)
        GpioPins.pin17.pwm(math.abs(x))
      }
    } else {
      MCspeed(500 * math.signum(x))
    }
    "OK"
  }
}

object GpioPins {
  val pin2 = DigitalPin(2, "pwm MB")
  val pin3 = DigitalPin(3, "break MB")
  val pin4 = DigitalPin(4, "direction MB")

  val pin27 = DigitalPin(27, "pwm MA")
  val pin22 = DigitalPin(22, "break MA")
  val pin23 = DigitalPin(23, "direction MA")

  val pin17 = DigitalPin(17, "pwm MC")
  val pin18 = DigitalPin(18, "pwm MC")

  val pin25 = DigitalPin(25, "front lights")
  val pin24 = DigitalPin(24, "side lights")
}
