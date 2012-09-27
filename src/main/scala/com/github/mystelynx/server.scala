package com.github.mystelynx

import unfiltered.request._
import unfiltered.netty.cycle._
import unfiltered.netty.ServerErrorResponse
import org.clapper.avsl.Logger
import unfiltered.response._
import unfiltered.scalate.Scalate
import javax.ws.rs
import unfiltered.response.Redirect
import dispatch.{Request, url}
import unfiltered.Cookie

/**
 * Created with IntelliJ IDEA.
 * User: urakawa
 * Date: 2012/09/26
 * Time: 22:01
 * To change this template use File | Settings | File Templates.
 */
class Nekopic extends Plan with ThreadPool with ServerErrorResponse {
  val CALLBACK_URL = "http://nekopic.herokuapp.com/oauth/callback"
  val log = Logger(classOf[Nekopic])

  def intent = {
    case req @ GET(Path("/")) => Ok ~> Html { <a href="/oauth/connect">Connect with Instagram</a>}
    case GET(Path("/oauth/connect")) => Redirect(Instagram.authorizeUrl(CALLBACK_URL))
    case req @ GET(Path("/oauth/callback")) & Params(InstagramAuthSuccess(code)) => {
      val resp = Instagram.getAccessToken(code)(CALLBACK_URL)
      SetCookies(Cookie("instagram_access_token", resp.access_token)) ~> Redirect("/feed")
    }
    case req @ GET(Path("/feed")) & Cookies(cs) if (cs("instagram_access_token") isDefined) => {
      val token = cs("instagram_access_token").map(_.value).getOrElse("???")
      val client = new Instagram.Client(token)
      val resp = client.users_self_feed

      ResponseString(resp.toString)
    }
    case GET(Path("/feed")) => Redirect("/")
  }
}

object InstagramAuthToken {
  val CLIENT_ID = "bd8b8f655d1547f396c9ed5a6fe9b795"
  val CLIENT_SECRET = "9c5115f3e86f480cad1fadd1753bb75f"
  val REDIRECT_URI = "http://nekopic.herokuapp.com/redirect"
}
object InstagramAuthSuccess extends Params.Extract("code", Params.first ~> Params.nonempty ~> Params.trimmed)
