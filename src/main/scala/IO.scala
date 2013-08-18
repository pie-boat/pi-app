package pieboat.pi

import pieboat.network.Messages
import com.pi4j.wiringpi.SoftPwm
import com.pi4j.io.gpio._

object IO {
  com.pi4j.wiringpi.Gpio.wiringPiSetupGpio();

  def setGPIOWith(s: String) = Messages.readAndExecute(
    ping = () => Messages.answerHeartbeat,
    status = () => "Not implemented",
    speed = x => "Not implemented",
    direction = x => "Direction: " + x,
    frontLights = on => this.frontLights(on),
    sideLights = on => this.sideLights(on),
    debugMA = x => this.debugMA(x),
    debugMB = x => this.debugMB(x),
    debugMC = x => this.debugMC(x)
  )(s)

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

  def debugMA(x: Integer) = {
    if(math.abs(x) <= 500) {
      GpioPins.pin22.low

      if (x >= 0) {
        GpioPins.pin23.high
      } else {
        GpioPins.pin23.low
      }

      SoftPwm.softPwmCreate(2, 0, 500)
      SoftPwm.softPwmWrite(2, math.abs(x))
      "OK"
    } else {
      "NOK"
    }
  }

  def debugMB(x: Integer) = {
    if(math.abs(x) <= 500) {
      GpioPins.pin3.low

      if (x >= 0) {
        GpioPins.pin4.high
      } else {
        GpioPins.pin4.low
      }

      SoftPwm.softPwmCreate(8, 0, 500)
      SoftPwm.softPwmWrite(8, math.abs(x))
      "OK"
    } else {
      "NOK"
    }
  }

  def debugMC(x: Integer) = {
    if(math.abs(x) <= 500) {
      if (x >= 0) {
        SoftPwm.softPwmCreate(0, 0, 500)
        SoftPwm.softPwmCreate(1, 0, 500)
        SoftPwm.softPwmWrite(1, math.abs(x))
      } else {
        SoftPwm.softPwmCreate(1, 0, 500)
        SoftPwm.softPwmCreate(0, 0, 500)
        SoftPwm.softPwmWrite(0, math.abs(x))
      }
      "OK"
    } else {
      "NOK"
    }
  }
}

object GpioPins {
  val gpio = GpioFactory.getInstance()

  val pin2 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_08) // pwm MB
  val pin3 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_09) // break MB
  val pin4 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_07) // direction MB

  val pin27 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02) // pwm MA
  val pin22 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03) // break MA
  val pin23 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04) // direction MA

  val pin17 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00) // pwm MC
  val pin18 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01) // pwm MC

  val pin25 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_06) // front lights
  val pin24 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_05) // side lights
}
