package pieboat.pi

import akka.actor._
import akka.util.Timeout
import java.util.Date

case object Ping
case object CheckPing

class State extends Actor {
  var lastPing = this._currentTime

  def _currentTime = (new Date).getTime

  def receive = {
    case Ping => println("Got pinged"); this.lastPing = this._currentTime
    case CheckPing => {
      print("Checking ping… ")
      if(this.lastPing < this._currentTime - 2000) {
        println("Nope, stopping motors…")
        IO.speed(0)
      } else {
        println("Ok")
      }
    }
  }
}

object State {
  var speed: Int = 0
  var direction: Int = 0
}
