package com.github.mystelynx

import org.specs._

/**
 * Created with IntelliJ IDEA.
 * User: urakawa
 * Date: 2012/09/27
 * Time: 0:00
 * To change this template use File | Settings | File Templates.
 */
object ServerSpecNetty extends unfiltered.spec.netty.Served with ServerSpec {
  def setup = { p =>
    unfiltered.netty.Http.local(p.port).handler(new Nekopic)
  }
}
trait ServerSpec extends unfiltered.spec.Hosted {
  import dispatch._
  import dispatch.Http._
  import unfiltered.filter._
  import unfiltered.request._
  import unfiltered.response._

  "top page" should {
    "include a link to auth page" in {
      val resp = Http(host as_str)
      resp must include("Connect with Instagram")

    }
  }

  "authenticate to Instagram" should {
    "get authorization code" in {
      val resp = Http(host / "oauth" / "connect" as_str)

    }
  }
}
