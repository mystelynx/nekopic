import unfiltered.request._
import unfiltered.response._

import org.clapper.avsl.Logger
import util.Properties
import unfiltered.netty.{ServerErrorResponse, cycle}

class Hello extends cycle.Plan with cycle.ThreadPool with ServerErrorResponse {
  val logger = Logger(classOf[App])

  def intent = {
    case Path(Seg("hello"::Nil)) & Params(params) => {
      ResponseString("Hello %s" format params("name").headOption.getOrElse("Unfiltered"))
    }
    case Path(_) => ResponseString("Hello from Unfiltered!")
  }
}

object Web extends App {
  val port = Properties.envOrElse("PORT", "8080").toInt
  println("Starting on port:%d" format port)
  unfiltered.netty.Http(port).plan(new Hello).run()
//  unfiltered.jetty.Http.anylocal.plan(new Hello).run { s =>
//    unfiltered.util.Browser.open( "http://127.0.0.1:%d/hello".format(s.port))
//  }
}