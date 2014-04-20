package pieboat.pi

import com.pi4j.io.gpio._
import com.pi4j.wiringpi.SoftPwm

case class DigitalPin(number: Int, hookedUpTo: String) {
  val _prefix = "[Pin " + number + " <" + hookedUpTo + ">]: "

  val _pin = if(Config.onPi) {
    val gpio = GpioFactory.getInstance()
    DigitalPin.pins.get(number) map { p =>
      gpio.provisionDigitalOutputPin(p)
    }
  } else {
    None
  }

  def high: Unit = {
    if(Config.debug) {
      println(_prefix + "high")
    }
    _pin foreach(_.high)
  } 
  
  def low: Unit = {
    if(Config.debug) {
      println(_prefix + "low")
    }
    _pin foreach(_.low)
  } 

  def pwm(value: Int): Unit = {
    if(Config.debug) {
      println(_prefix + "PWM " + value + "/500")
    }
    for {
      p <- _pin
      gpio <- DigitalPin.pins.get(number)
    } yield {
      SoftPwm.softPwmCreate(gpio.getAddress(), 0, 500)
      SoftPwm.softPwmWrite(gpio.getAddress(), value)
    }
  }
  
}

object DigitalPin {
  val pins = Map(
    2 -> RaspiPin.GPIO_08,
    3 -> RaspiPin.GPIO_09,
    4 -> RaspiPin.GPIO_07,
    27 -> RaspiPin.GPIO_02,
    22 -> RaspiPin.GPIO_03,
    23 -> RaspiPin.GPIO_04,
    17 -> RaspiPin.GPIO_00,
    18 -> RaspiPin.GPIO_01,
    25 -> RaspiPin.GPIO_06,
    24 -> RaspiPin.GPIO_05
  )
}
