package pieboat.pi

import java.net.ServerSocket
import java.io._
import scala.util.{Try, Success, Failure}
import akka.actor._
import scala.concurrent.duration._

object Main extends App {
  val system = ActorSystem("pieboat")
  val stateActor = system.actorOf(Props(classOf[State]))

  import system.dispatcher // execution context

  Try {
    new ServerSocket(Config.port)
  } match {
    case Failure(e) => println(e.getMessage)
    case Success(server) => {
      while(true) {
        println("Listening on " + Config.port + ", awaiting remote…")
        val client = server.accept
        val in = Stream.continually(
          (new BufferedReader(new InputStreamReader(client.getInputStream))).readLine
        )
        val out = new PrintStream(client.getOutputStream)
        println("Remote connected!")

        val pingChecker = system.scheduler.schedule(
          0 seconds,
          1 seconds,
          stateActor,
          CheckPing
        )

        in takeWhile(_ != null) foreach { s =>
          out.println(Option(s) map(IO.setGPIOWith) getOrElse "?")
        }

        pingChecker.cancel

        println("Remote left")
        client.close
      }
      server.close
    }
  }
}
