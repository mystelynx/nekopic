package com.github.mystelynx

/**
 * Created with IntelliJ IDEA.
 * User: urakawa
 * Date: 2012/09/26
 * Time: 21:54
 * To change this template use File | Settings | File Templates.
 */
import org.specs._
import unfiltered.request.{GET, Params}

object InstagramAuthSpecNetty extends unfiltered.spec.netty.Planned with InstagramAuthSpec

trait InstagramAuthSpec extends unfiltered.spec.Hosted {
  import unfiltered.response._
  import unfiltered.request.{Path => UFPath, _}

  import dispatch._

  def intent[A, B]: unfiltered.Cycle.Intent[A,B] = {
    case UFPath("/success") & Params(InstagramAuthSuccess(code)) => ResponseString(code)
  }

  "Instagram-auth success" should {
    "contains 'code' param" in {
      http(host / "success" << Map("code"->"hogehoge") as_str) must_== "hogehoge"
    }
  }
}
