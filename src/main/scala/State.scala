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
    case Ping => this.lastPing = this._currentTime
    case CheckPing => {
      if(this.lastPing < this._currentTime - 2000) {
        IO.speed(0)
      }
    }
  }
}
