package pieboat.pi

import java.net.ServerSocket
import java.io._
import scala.util.{Try, Success, Failure}


object Main extends App {
  Try {
    new ServerSocket(Configuration.port)
  } match {
    case Failure(e) => println(e.getMessage)
    case Success(server) => {
      while(true) {
        println("Listening on " + Configuration.port + ", awaiting remote…")
        val client = server.accept
        val in = Stream.continually(
          (new BufferedReader(new InputStreamReader(client.getInputStream))).readLine
        )
        val out = new PrintStream(client.getOutputStream)
        println("Remote connected!")

        in takeWhile(_ != null) foreach { s =>
          out.println(Option(s) map(IO.setGPIOWith) getOrElse "?")
        }

        println("Remote left")
        client.close
      }
      server.close
    }
  }
}
