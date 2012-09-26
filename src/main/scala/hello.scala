import unfiltered.request._
import unfiltered.response._

import org.clapper.avsl.Logger
import util.Properties
import unfiltered.netty.{ServerErrorResponse, cycle}

class Hello extends cycle.Plan with cycle.ThreadPool with ServerErrorResponse {
  val logger = Logger(classOf[App])

  val CLIENT_ID = "bd8b8f655d1547f396c9ed5a6fe9b795"
  val CLIENT_SECRET = "9c5115f3e86f480cad1fadd1753bb75f"
  val REDIRECT_URI = "http://nekopic.herokuapp.com/redirect"

  def intent = {
    case GET(Path("/auth")) => {
      Ok ~> Html5 {
        <html>
          <body>
            <a href={"https://api.instagram.com/oauth/authorize/?client_id="+CLIENT_ID+"&redirect_uri="+REDIRECT_URI+"&response_type=code"}>
              instagram authentication
            </a>
          </body>
        </html>
      }
    }
    case GET(Path("/redirect")) & Params(InstagramAuthSuccess(code)) => {
      Ok ~> Html5 {
        <html>
          <body>
            <p>code: {code}</p>
          </body>
        </html>
      }
    }
    case Path(Seg("hello"::Nil)) & Params(params) => {

      ResponseString("Hello %s" format params("name").headOption.getOrElse("Unfiltered"))
    }
    case Path(_) => ResponseString("Hello from Unfiltered!!!!")
  }
}

object InstagramAuthSuccess extends Params.Extract("code", Params.first ~> Params.nonempty)
object InstagramAuthFailure extends Params.Extract("error", Params.first ~> Params.nonempty)

object Web extends App {
  val port = Properties.envOrElse("PORT", "8080").toInt
  println("Starting on port:%d" format port)
  unfiltered.netty.Http(port).plan(new Hello).run()
//  unfiltered.jetty.Http.anylocal.plan(new Hello).run { s =>
//    unfiltered.util.Browser.open( "http://127.0.0.1:%d/hello".format(s.port))
//  }
}