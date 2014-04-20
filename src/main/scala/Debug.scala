package pieboat.pi.debug

import pieboat.pi.Config

case class DummyPin(number: Int, hookedUpTo: String) {
  val _prefix = "[Pin " + number + " <" + hookedUpTo + ">]: "
  def high: Unit = if(Config.debug) { println(_prefix + "high") }
  def low: Unit = if(Config.debug) { println(_prefix + "low") }
}

