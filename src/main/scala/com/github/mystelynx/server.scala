package com.github.mystelynx

import unfiltered.request._
import unfiltered.netty.cycle._
import unfiltered.netty.ServerErrorResponse
import org.clapper.avsl.Logger
import unfiltered.response._
import unfiltered.response.Redirect
import unfiltered.Cookie
import sun.misc.OSEnvironment

/**
 * Created with IntelliJ IDEA.
 * User: urakawa
 * Date: 2012/09/26
 * Time: 22:01
 * To change this template use File | Settings | File Templates.
 */
class Nekopic extends unfiltered.filter.Plan {
  val CALLBACK_URL = "http://nekopic.herokuapp.com/oauth/callback"
  val log = Logger(classOf[Nekopic])

  import SessionStore._

  def intent = {
    case req @ GET(Path("/")) => {
      val sid = createSession
      Ok ~> SetCookies(Cookie("sid", sid)) ~> Scalate("index.jade")
    }
    case GET(Path("/oauth/connect")) => Redirect(Instagram.authorizeUrl(CALLBACK_URL))
    case req @ GET(Path("/oauth/callback")) & Params(InstagramAuthSuccess(code)) & Cookies(cs) => {
      val resp = Instagram.getAccessToken(code)(CALLBACK_URL)
      val sid = cs("sid").map(_.value).getOrElse("unknown session id")
      withSession(sid) { _ save "ins"->resp.access_token }
      Redirect("/feed")
    }
    case req @ GET(Path("/feed")) & Cookies(cs) => {
      val sid = cs("sid").map(_.value).getOrElse("unknown session id")
      val token = withSession(sid) { _ getString "ins" }
      val client = new Instagram.Client(token)
      val resp = client.users_self_feed

      Ok ~> Scalate("feed.jade", "feed"->resp)
    }
  }
}

object Nekopic {
  import org.fusesource.scalate._
  val scalate = new TemplateEngine
}

object InstagramAuthToken {
  val CLIENT_ID = "bd8b8f655d1547f396c9ed5a6fe9b795"
  val CLIENT_SECRET = "9c5115f3e86f480cad1fadd1753bb75f"
  val REDIRECT_URI = "http://nekopic.herokuapp.com/redirect"
}
object InstagramAuthSuccess extends Params.Extract("code", Params.first ~> Params.nonempty ~> Params.trimmed)

object SessionStore {
  protected def generate = scala.util.Random.alphanumeric.take(256).mkString
  import scala.collection._
  private val store = mutable.Map[String, Session]()

  def createSession = {
    val id = generate
    val emptySession = new Session
    store += id->emptySession
    id
  }

  def withSession[T](id: String)(block: Session => T): T = {
    val s = store get id getOrElse {
      throw new IllegalStateException("session is not created")
    }
    block(s)
  }

  class Session {
    private val storage = mutable.Map[String, Any]()

    def save(kv: Pair[String, Any]) = storage += kv
    def remove(k: String) = storage -= k
    def get(k: String) = storage get k
    def getString(k: String) = get(k) map(_.toString) getOrElse { throw new IllegalStateException("invalid data") }
  }
}

object SimpleSessionStore {
  import scala.collection._
  private val store = mutable.Map[String, mutable.Map[String, Any]]()

  def createSession = {
    val id = generate
    SimpleSessionStore.synchronized {
      store += (id->mutable.Map[String, Any]())
    }
    id
  }

  def getSession(id: String) = store get id
  def removeSession(id: String) = store remove id
  def setSessionAttribute(id: String, key: String, value: Any) = store get id match {
    case Some(map) => map += (key->value)
    case _ => throw new IllegalStateException("session store not created")
  }
  def getSessionAttribute(id: String, key: String) = store get id match {
    case Some(map) => map get key
    case _ => throw new IllegalStateException("session store not created")
  }
  def removeSessionAttribute(id: String, key: String) = store get id match {
    case Some(map) => map remove key
    case _ => throw new IllegalStateException("session store not created")
  }

  protected def generate = scala.util.Random.alphanumeric.take(256).mkString
}