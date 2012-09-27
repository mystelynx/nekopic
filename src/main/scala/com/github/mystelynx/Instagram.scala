package com.github.mystelynx

import dispatch.url
import tools.nsc.doc.html.Page
import net.liftweb.json.DefaultFormats

/**
 * Created with IntelliJ IDEA.
 * User: urakawa
 * Date: 2012/09/27
 * Time: 15:48
 * To change this template use File | Settings | File Templates.
 */
object Instagram {
  implicit val formats = DefaultFormats

  val clientId = "bd8b8f655d1547f396c9ed5a6fe9b795"
  val clientSecret = "9c5115f3e86f480cad1fadd1753bb75f"
  def authorizeUrl(redirectUri: String) =
    "https://api.instagram.com/oauth/authorize/?client_id="+clientId+"&redirect_uri="+redirectUri+"&response_type=code"

  def getAccessToken(code: String)(redirectUri: String) = {
    val req = url("https://api.instagram.com/oauth/access_token").POST <<
      Map("client_id"->clientId, "client_secret"->clientSecret,
        "grant_type"->"authorization_code", "redirect_uri"->redirectUri, "code"->code) <:<
      Map("Accept"->"application/json")

    val h = new dispatch.Http
    val resp = h(req as_str)

    import net.liftweb.json._
    val json = parse(resp)

    json.extract[InstagramAuthInfo]
  }

  class Client(accessToken: String) {
    def users_self_feed = {
      val req = url("https://api.instagram.com/v1/users/self/feed") <<?
        Map("access_token"->accessToken) <:< Map("Accept"->"application/json")
      val h = new dispatch.Http
      val resp = h(req as_str)

      import net.liftweb.json._
      val json = parse(resp)

      json.extract[UsersSelfFeed]
    }
  }
}

case class InstagramUser(id: String, username: String, full_name: String, profile_picture: String)
case class InstagramAuthInfo(access_token: String, user: InstagramUser)

case class Pagenation(next_url: String, next_max_id: String)
case class Meta(code: Int)
case class Type(value: String)
case class Location(value: String)
case class Comment(created_time: String, text: String, from: InstagramUser, id: String)
case class Comments(count: Int, data: List[Comment])
case class Like(from: InstagramUser)
case class Likes(count: Int, data: List[Like])
case class Image(url: String, width: Int, height: String)
case class Images(low_resolution: Image, thumbnail: Image, standard_resolution: Image)
case class Caption(created_time: String, text: String, from: InstagramUser, id: String)
case class InstagramUserDetail(id: String, username: String, full_name: String, profile_picture: String,
                               website: String, bio: String)
case class Data(attribution: String, tags: List[String], type_ : String, location: String, comments: Comments,
                 filter: String, created_time: String, link: String, likes: Likes, images: Images, caption: Caption,
                 user_has_liked: Boolean, id: String, user: InstagramUserDetail)
case class UsersSelfFeed(pagenation: Pagenation, meta: Meta, data: List[Data])