package com.github.mystelynx

/**
 * Created with IntelliJ IDEA.
 * User: urakawa
 * Date: 2012/09/26
 * Time: 21:54
 * To change this template use File | Settings | File Templates.
 */
import org.specs._
import mock.Mockito
import org.mockito.Mockito._
import org.mockito.Matchers._
import org.powermock.api.mockito.PowerMockito._
import unfiltered.request.{GET, Params}
import org.powermock.api.mockito.PowerMockito

object InstagramAuthSpecNetty extends unfiltered.spec.netty.Planned with InstagramAuthSpec

trait InstagramAuthSpec extends unfiltered.spec.Hosted with Mockito {
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

  import net.liftweb.json._
  implicit val formats = DefaultFormats

  "json extraction" should {
    "authentication" in {
      val jsonString =
        """
          |{
          |    "access_token": "fb2e77d.47a0479900504cb3ab4a1f626d174d2d",
          |    "user": {
          |        "id": "1574083",
          |        "username": "snoopdogg",
          |        "full_name": "Snoop Dogg",
          |        "profile_picture": "http://distillery.s3.amazonaws.com/profiles/profile_1574083_75sq_1295469061.jpg"
          |    }
          |}
        """.stripMargin

      val json = parse(jsonString)
      val result = json.extract[InstagramAuthInfo]
      result mustNotBe null
    }

    "pagination" in {
      val jsonString =
        """
          |{
          |  "next_url": "https://api.instagram.com/v1/users/self/feed?access_token=13924824.1fb234f.7833e0b101bf407da2b548f1221c2146&count=&min_id=&max_id=289972958982048194_204601537",
          |  "next_max_id": "289972958982048194_204601537"
          |}
        """.stripMargin

      val json = parse(jsonString)
      val result = json.extract[Pagination]
      result mustNotBe null
    }

    "meta" in {
      val jsonString =
        """
          |{
          |  "code": 200
          |}
        """.stripMargin

      val json = parse(jsonString)
      val result = json.extract[Meta]
      result mustNotBe null
    }

    "data" in {
      val jsonString =
        """
          |{
          |      "attribution": null,
          |      "tags":  [],
          |      "type": "image",
          |      "location":  {
          |        "latitude": 35.740441345,
          |        "name": "三宝寺池",
          |        "longitude": 139.594087872,
          |        "id": 60494
          |      },
          |      "comments":  {
          |        "count": 0,
          |        "data":  []
          |      },
          |      "filter": "Normal",
          |      "created_time": "1348789229",
          |      "link": "http://instagr.am/p/QGPh_qIZoE/",
          |      "likes":  {
          |        "count": 28,
          |        "data":  [
          |           {
          |            "username": "poyontmk",
          |            "profile_picture": "http://images.instagram.com/profiles/profile_12221120_75sq_1322723632.jpg",
          |            "id": "12221120",
          |            "full_name": "poyontmk"
          |          },
          |           {
          |            "username": "bitsan",
          |            "profile_picture": "http://images.instagram.com/profiles/profile_290016_75sq_1288106174.jpg",
          |            "id": "290016",
          |            "full_name": "bitsan"
          |          },
          |           {
          |            "username": "jessicarabbit33",
          |            "profile_picture": "http://images.instagram.com/profiles/profile_597451_75sq_1337491379.jpg",
          |            "id": "597451",
          |            "full_name": "Jessica"
          |          },
          |           {
          |            "username": "satomichacha",
          |            "profile_picture": "http://images.instagram.com/profiles/profile_182098598_75sq_1348201067.jpg",
          |            "id": "182098598",
          |            "full_name": "satomichacha"
          |          },
          |           {
          |            "username": "rapepikachu",
          |            "profile_picture": "http://images.instagram.com/profiles/profile_11432807_75sq_1344340178.jpg",
          |            "id": "11432807",
          |            "full_name": "Melinna Amezquita"
          |          },
          |           {
          |            "username": "turning_graves",
          |            "profile_picture": "http://images.instagram.com/profiles/profile_11999804_75sq_1348388205.jpg",
          |            "id": "11999804",
          |            "full_name": "🐱Maggot Catnip"
          |          },
          |           {
          |            "username": "kakaloklok",
          |            "profile_picture": "http://images.instagram.com/profiles/profile_35903498_75sq_1335669591.jpg",
          |            "id": "35903498",
          |            "full_name": "kakaloklok"
          |          },
          |           {
          |            "username": "asa818",
          |            "profile_picture": "http://images.instagram.com/profiles/profile_31851499_75sq_1333516603.jpg",
          |            "id": "31851499",
          |            "full_name": "mariko Simose"
          |          },
          |           {
          |            "username": "limtsack",
          |            "profile_picture": "http://images.instagram.com/profiles/profile_11938737_75sq_1347011150.jpg",
          |            "id": "11938737",
          |            "full_name": "🐱 Linn"
          |          },
          |           {
          |            "username": "oshioki",
          |            "profile_picture": "http://images.instagram.com/profiles/profile_1300412_75sq_1297871206.jpg",
          |            "id": "1300412",
          |            "full_name": "oshioki└╹▿╹┘"
          |          }
          |        ]
          |      },
          |      "images":  {
          |        "low_resolution":  {
          |          "url": "http://distilleryimage0.s3.amazonaws.com/b8806e2e08fc11e2a84922000a1e8bad_6.jpg",
          |          "width": 306,
          |          "height": 306
          |        },
          |        "thumbnail":  {
          |          "url": "http://distilleryimage0.s3.amazonaws.com/b8806e2e08fc11e2a84922000a1e8bad_5.jpg",
          |          "width": 150,
          |          "height": 150
          |        },
          |        "standard_resolution":  {
          |          "url": "http://distilleryimage0.s3.amazonaws.com/b8806e2e08fc11e2a84922000a1e8bad_7.jpg",
          |          "width": 612,
          |          "height": 612
          |        }
          |      },
          |      "caption":  {
          |        "created_time": "1348789498",
          |        "text": "今朝は涼しいね！Good morning, Otousan and Densuke!! Sep.28.2012.",
          |        "from":  {
          |          "username": "pandastyle",
          |          "profile_picture": "http://images.instagram.com/profiles/profile_937149_75sq_1306457312.jpg",
          |          "id": "937149",
          |          "full_name": "PandaStyle"
          |        },
          |        "id": "289989790414248744"
          |      },
          |      "user_has_liked": false,
          |      "id": "289987532804954628_937149",
          |      "user":  {
          |        "username": "pandastyle",
          |        "website": "http://travel.moo.jp",
          |        "bio": "🐾猫とパンダ.Panda & Cat.\r\n🗼Tokyo.Japanese.editor ( ‾ʖ̫‾)\r\n🐈I take photo of StrayCat in Tokyo.\r\n🐞都内石神井公園(三宝寺池)の猫を撮影",
          |        "profile_picture": "http://images.instagram.com/profiles/profile_937149_75sq_1306457312.jpg",
          |        "full_name": "PandaStyle",
          |        "id": "937149"
          |      }
          |    }
        """.stripMargin

      val json = parse(jsonString)
      val result = json.extract[Data]

      result mustNotBe null
    }

    "users_self_feed" in {
      val jsonString =
        """
                         |{
                         |  "pagination":  {
                         |    "next_url": "https://api.instagram.com/v1/users/self/feed?access_token=13924824.1fb234f.7833e0b101bf407da2b548f1221c2146&count=&min_id=&max_id=289972958982048194_204601537",
                         |    "next_max_id": "289972958982048194_204601537"
                         |  },
                         |  "meta":  {
                         |    "code": 200
                         |  },
                         |  "data":  [
                         |     {
                         |      "attribution": null,
                         |      "tags":  [],
                         |      "type": "image",
                         |      "location":  {
                         |        "latitude": 35.740441345,
                         |        "name": "三宝寺池",
                         |        "longitude": 139.594087872,
                         |        "id": 60494
                         |      },
                         |      "comments":  {
                         |        "count": 0,
                         |        "data":  []
                         |      },
                         |      "filter": "Normal",
                         |      "created_time": "1348789229",
                         |      "link": "http://instagr.am/p/QGPh_qIZoE/",
                         |      "likes":  {
                         |        "count": 28,
                         |        "data":  [
                         |           {
                         |            "username": "poyontmk",
                         |            "profile_picture": "http://images.instagram.com/profiles/profile_12221120_75sq_1322723632.jpg",
                         |            "id": "12221120",
                         |            "full_name": "poyontmk"
                         |          },
                         |           {
                         |            "username": "bitsan",
                         |            "profile_picture": "http://images.instagram.com/profiles/profile_290016_75sq_1288106174.jpg",
                         |            "id": "290016",
                         |            "full_name": "bitsan"
                         |          },
                         |           {
                         |            "username": "jessicarabbit33",
                         |            "profile_picture": "http://images.instagram.com/profiles/profile_597451_75sq_1337491379.jpg",
                         |            "id": "597451",
                         |            "full_name": "Jessica"
                         |          },
                         |           {
                         |            "username": "satomichacha",
                         |            "profile_picture": "http://images.instagram.com/profiles/profile_182098598_75sq_1348201067.jpg",
                         |            "id": "182098598",
                         |            "full_name": "satomichacha"
                         |          },
                         |           {
                         |            "username": "rapepikachu",
                         |            "profile_picture": "http://images.instagram.com/profiles/profile_11432807_75sq_1344340178.jpg",
                         |            "id": "11432807",
                         |            "full_name": "Melinna Amezquita"
                         |          },
                         |           {
                         |            "username": "turning_graves",
                         |            "profile_picture": "http://images.instagram.com/profiles/profile_11999804_75sq_1348388205.jpg",
                         |            "id": "11999804",
                         |            "full_name": "🐱Maggot Catnip"
                         |          },
                         |           {
                         |            "username": "kakaloklok",
                         |            "profile_picture": "http://images.instagram.com/profiles/profile_35903498_75sq_1335669591.jpg",
                         |            "id": "35903498",
                         |            "full_name": "kakaloklok"
                         |          },
                         |           {
                         |            "username": "asa818",
                         |            "profile_picture": "http://images.instagram.com/profiles/profile_31851499_75sq_1333516603.jpg",
                         |            "id": "31851499",
                         |            "full_name": "mariko Simose"
                         |          },
                         |           {
                         |            "username": "limtsack",
                         |            "profile_picture": "http://images.instagram.com/profiles/profile_11938737_75sq_1347011150.jpg",
                         |            "id": "11938737",
                         |            "full_name": "🐱 Linn"
                         |          },
                         |           {
                         |            "username": "oshioki",
                         |            "profile_picture": "http://images.instagram.com/profiles/profile_1300412_75sq_1297871206.jpg",
                         |            "id": "1300412",
                         |            "full_name": "oshioki└╹▿╹┘"
                         |          }
                         |        ]
                         |      },
                         |      "images":  {
                         |        "low_resolution":  {
                         |          "url": "http://distilleryimage0.s3.amazonaws.com/b8806e2e08fc11e2a84922000a1e8bad_6.jpg",
                         |          "width": 306,
                         |          "height": 306
                         |        },
                         |        "thumbnail":  {
                         |          "url": "http://distilleryimage0.s3.amazonaws.com/b8806e2e08fc11e2a84922000a1e8bad_5.jpg",
                         |          "width": 150,
                         |          "height": 150
                         |        },
                         |        "standard_resolution":  {
                         |          "url": "http://distilleryimage0.s3.amazonaws.com/b8806e2e08fc11e2a84922000a1e8bad_7.jpg",
                         |          "width": 612,
                         |          "height": 612
                         |        }
                         |      },
                         |      "caption":  {
                         |        "created_time": "1348789498",
                         |        "text": "今朝は涼しいね！Good morning, Otousan and Densuke!! Sep.28.2012.",
                         |        "from":  {
                         |          "username": "pandastyle",
                         |          "profile_picture": "http://images.instagram.com/profiles/profile_937149_75sq_1306457312.jpg",
                         |          "id": "937149",
                         |          "full_name": "PandaStyle"
                         |        },
                         |        "id": "289989790414248744"
                         |      },
                         |      "user_has_liked": false,
                         |      "id": "289987532804954628_937149",
                         |      "user":  {
                         |        "username": "pandastyle",
                         |        "website": "http://travel.moo.jp",
                         |        "bio": "🐾猫とパンダ.Panda & Cat.\r\n🗼Tokyo.Japanese.editor ( ‾ʖ̫‾)\r\n🐈I take photo of StrayCat in Tokyo.\r\n🐞都内石神井公園(三宝寺池)の猫を撮影",
                         |        "profile_picture": "http://images.instagram.com/profiles/profile_937149_75sq_1306457312.jpg",
                         |        "full_name": "PandaStyle",
                         |        "id": "937149"
                         |      }
                         |    },
                         |     {
                         |      "attribution": null,
                         |      "tags":  [
                         |        "neko",
                         |        "cats",
                         |        "gato",
                         |        "cat"
                         |      ],
                         |      "type": "image",
                         |      "location":  {
                         |        "latitude": -23.962617874,
                         |        "longitude": -46.331356048
                         |      },
                         |      "comments":  {
                         |        "count": 2,
                         |        "data":  [
                         |           {
                         |            "created_time": "1348789584",
                         |            "text": "@tatianaroso olha o Hiro fofona!!! 😀😋😻",
                         |            "from":  {
                         |              "username": "cris_kato",
                         |              "profile_picture": "http://images.instagram.com/profiles/profile_10818105_75sq_1318383134.jpg",
                         |              "id": "10818105",
                         |              "full_name": "cris_kato"
                         |            },
                         |            "id": "289990511869070319"
                         |          },
                         |           {
                         |            "created_time": "1348789993",
                         |            "text": "Ahhh não gente!!!! Vou entrar na sua casa e roubar o HIRO prá mim japinha @cris_kato!!! Ele é um goxtosoooo!!!",
                         |            "from":  {
                         |              "username": "tatianaroso",
                         |              "profile_picture": "http://images.instagram.com/profiles/profile_3558556_75sq_1344426261.jpg",
                         |              "id": "3558556",
                         |              "full_name": "tatianaroso"
                         |            },
                         |            "id": "289993944546183225"
                         |          }
                         |        ]
                         |      },
                         |      "filter": "Normal",
                         |      "created_time": "1348789406",
                         |      "link": "http://instagr.am/p/QGP3k7MIvT/",
                         |      "likes":  {
                         |        "count": 14,
                         |        "data":  [
                         |           {
                         |            "username": "ramnit20",
                         |            "profile_picture": "http://images.instagram.com/profiles/profile_32677763_75sq_1337131454.jpg",
                         |            "id": "32677763",
                         |            "full_name": "Tinmar Hernandez"
                         |          },
                         |           {
                         |            "username": "out_to_lunch",
                         |            "profile_picture": "http://images.instagram.com/profiles/profile_23092230_75sq_1347209646.jpg",
                         |            "id": "23092230",
                         |            "full_name": "Esme Salinger"
                         |          },
                         |           {
                         |            "username": "rpmckay",
                         |            "profile_picture": "http://images.instagram.com/profiles/profile_14325097_75sq_1340697502.jpg",
                         |            "id": "14325097",
                         |            "full_name": "Ryan"
                         |          },
                         |           {
                         |            "username": "chefpandita",
                         |            "profile_picture": "http://images.instagram.com/profiles/profile_39341_75sq_1348659110.jpg",
                         |            "id": "39341",
                         |            "full_name": "🐼Yuri @chefpandita"
                         |          },
                         |           {
                         |            "username": "jacobmurphy89",
                         |            "profile_picture": "http://images.instagram.com/profiles/profile_175515982_75sq_1338347408.jpg",
                         |            "id": "175515982",
                         |            "full_name": "Jacob Murphy"
                         |          },
                         |           {
                         |            "username": "freddynava",
                         |            "profile_picture": "http://images.instagram.com/profiles/profile_5337743_75sq_1314976548.jpg",
                         |            "id": "5337743",
                         |            "full_name": "Freddy Nava 💂®"
                         |          },
                         |           {
                         |            "username": "sumi_sunshine",
                         |            "profile_picture": "http://images.instagram.com/profiles/profile_34876082_75sq_1338785664.jpg",
                         |            "id": "34876082",
                         |            "full_name": "Summer Ferrier"
                         |          },
                         |           {
                         |            "username": "shesfromaway",
                         |            "profile_picture": "http://images.instagram.com/profiles/profile_2272826_75sq_1346685630.jpg",
                         |            "id": "2272826",
                         |            "full_name": "ShesFromAway"
                         |          },
                         |           {
                         |            "username": "kimmcash13",
                         |            "profile_picture": "http://images.instagram.com/profiles/profile_4446299_75sq_1338311221.jpg",
                         |            "id": "4446299",
                         |            "full_name": "Kim Cash"
                         |          },
                         |           {
                         |            "username": "tatianaroso",
                         |            "profile_picture": "http://images.instagram.com/profiles/profile_3558556_75sq_1344426261.jpg",
                         |            "id": "3558556",
                         |            "full_name": "tatianaroso"
                         |          }
                         |        ]
                         |      },
                         |      "images":  {
                         |        "low_resolution":  {
                         |          "url": "http://distilleryimage3.s3.amazonaws.com/21e31b7808fd11e2877122000a1fbc4f_6.jpg",
                         |          "width": 306,
                         |          "height": 306
                         |        },
                         |        "thumbnail":  {
                         |          "url": "http://distilleryimage3.s3.amazonaws.com/21e31b7808fd11e2877122000a1fbc4f_5.jpg",
                         |          "width": 150,
                         |          "height": 150
                         |        },
                         |        "standard_resolution":  {
                         |          "url": "http://distilleryimage3.s3.amazonaws.com/21e31b7808fd11e2877122000a1fbc4f_7.jpg",
                         |          "width": 612,
                         |          "height": 612
                         |        }
                         |      },
                         |      "caption":  {
                         |        "created_time": "1348789442",
                         |        "text": "Hiro!!! #cat #cats #gato #neko",
                         |        "from":  {
                         |          "username": "cris_kato",
                         |          "profile_picture": "http://images.instagram.com/profiles/profile_10818105_75sq_1318383134.jpg",
                         |          "id": "10818105",
                         |          "full_name": "cris_kato"
                         |        },
                         |        "id": "289989319545883598"
                         |      },
                         |      "user_has_liked": false,
                         |      "id": "289989015928605651_10818105",
                         |      "user":  {
                         |        "username": "cris_kato",
                         |        "website": "",
                         |        "bio": "Walk on!!!! ",
                         |        "profile_picture": "http://images.instagram.com/profiles/profile_10818105_75sq_1318383134.jpg",
                         |        "full_name": "cris_kato",
                         |        "id": "10818105"
                         |      }
                         |    }
                         |  ]
                         |}
                         |
                       """.stripMargin
      val json = parse(jsonString)
      val result = json.extract[UsersSelfFeed]

      result mustNotBe null
    }
  }

  "users-self-feed" should {
    "returns my feed" in {
      mockStatic(Instagram.Client.getClass)
      PowerMockito.when(Instagram.Client.getClass, "call", anyString(), anyString()) thenReturn ("abc")

/*
        """
          |{
          |  "pagination":  {
          |    "next_url": "https://api.instagram.com/v1/users/self/feed?access_token=13924824.1fb234f.7833e0b101bf407da2b548f1221c2146&count=&min_id=&max_id=289972958982048194_204601537",
          |    "next_max_id": "289972958982048194_204601537"
          |  },
          |  "meta":  {
          |    "code": 200
          |  },
          |  "data":  [
          |     {
          |      "attribution": null,
          |      "tags":  [],
          |      "type": "image",
          |      "location":  {
          |        "latitude": 35.740441345,
          |        "name": "三宝寺池",
          |        "longitude": 139.594087872,
          |        "id": 60494
          |      },
          |      "comments":  {
          |        "count": 0,
          |        "data":  []
          |      },
          |      "filter": "Normal",
          |      "created_time": "1348789229",
          |      "link": "http://instagr.am/p/QGPh_qIZoE/",
          |      "likes":  {
          |        "count": 28,
          |        "data":  [
          |           {
          |            "username": "poyontmk",
          |            "profile_picture": "http://images.instagram.com/profiles/profile_12221120_75sq_1322723632.jpg",
          |            "id": "12221120",
          |            "full_name": "poyontmk"
          |          },
          |           {
          |            "username": "bitsan",
          |            "profile_picture": "http://images.instagram.com/profiles/profile_290016_75sq_1288106174.jpg",
          |            "id": "290016",
          |            "full_name": "bitsan"
          |          },
          |           {
          |            "username": "jessicarabbit33",
          |            "profile_picture": "http://images.instagram.com/profiles/profile_597451_75sq_1337491379.jpg",
          |            "id": "597451",
          |            "full_name": "Jessica"
          |          },
          |           {
          |            "username": "satomichacha",
          |            "profile_picture": "http://images.instagram.com/profiles/profile_182098598_75sq_1348201067.jpg",
          |            "id": "182098598",
          |            "full_name": "satomichacha"
          |          },
          |           {
          |            "username": "rapepikachu",
          |            "profile_picture": "http://images.instagram.com/profiles/profile_11432807_75sq_1344340178.jpg",
          |            "id": "11432807",
          |            "full_name": "Melinna Amezquita"
          |          },
          |           {
          |            "username": "turning_graves",
          |            "profile_picture": "http://images.instagram.com/profiles/profile_11999804_75sq_1348388205.jpg",
          |            "id": "11999804",
          |            "full_name": "🐱Maggot Catnip"
          |          },
          |           {
          |            "username": "kakaloklok",
          |            "profile_picture": "http://images.instagram.com/profiles/profile_35903498_75sq_1335669591.jpg",
          |            "id": "35903498",
          |            "full_name": "kakaloklok"
          |          },
          |           {
          |            "username": "asa818",
          |            "profile_picture": "http://images.instagram.com/profiles/profile_31851499_75sq_1333516603.jpg",
          |            "id": "31851499",
          |            "full_name": "mariko Simose"
          |          },
          |           {
          |            "username": "limtsack",
          |            "profile_picture": "http://images.instagram.com/profiles/profile_11938737_75sq_1347011150.jpg",
          |            "id": "11938737",
          |            "full_name": "🐱 Linn"
          |          },
          |           {
          |            "username": "oshioki",
          |            "profile_picture": "http://images.instagram.com/profiles/profile_1300412_75sq_1297871206.jpg",
          |            "id": "1300412",
          |            "full_name": "oshioki└╹▿╹┘"
          |          }
          |        ]
          |      },
          |      "images":  {
          |        "low_resolution":  {
          |          "url": "http://distilleryimage0.s3.amazonaws.com/b8806e2e08fc11e2a84922000a1e8bad_6.jpg",
          |          "width": 306,
          |          "height": 306
          |        },
          |        "thumbnail":  {
          |          "url": "http://distilleryimage0.s3.amazonaws.com/b8806e2e08fc11e2a84922000a1e8bad_5.jpg",
          |          "width": 150,
          |          "height": 150
          |        },
          |        "standard_resolution":  {
          |          "url": "http://distilleryimage0.s3.amazonaws.com/b8806e2e08fc11e2a84922000a1e8bad_7.jpg",
          |          "width": 612,
          |          "height": 612
          |        }
          |      },
          |      "caption":  {
          |        "created_time": "1348789498",
          |        "text": "今朝は涼しいね！Good morning, Otousan and Densuke!! Sep.28.2012.",
          |        "from":  {
          |          "username": "pandastyle",
          |          "profile_picture": "http://images.instagram.com/profiles/profile_937149_75sq_1306457312.jpg",
          |          "id": "937149",
          |          "full_name": "PandaStyle"
          |        },
          |        "id": "289989790414248744"
          |      },
          |      "user_has_liked": false,
          |      "id": "289987532804954628_937149",
          |      "user":  {
          |        "username": "pandastyle",
          |        "website": "http://travel.moo.jp",
          |        "bio": "🐾猫とパンダ.Panda & Cat.\r\n🗼Tokyo.Japanese.editor ( ‾ʖ̫‾)\r\n🐈I take photo of StrayCat in Tokyo.\r\n🐞都内石神井公園(三宝寺池)の猫を撮影",
          |        "profile_picture": "http://images.instagram.com/profiles/profile_937149_75sq_1306457312.jpg",
          |        "full_name": "PandaStyle",
          |        "id": "937149"
          |      }
          |    },
          |     {
          |      "attribution": null,
          |      "tags":  [
          |        "neko",
          |        "cats",
          |        "gato",
          |        "cat"
          |      ],
          |      "type": "image",
          |      "location":  {
          |        "latitude": -23.962617874,
          |        "longitude": -46.331356048
          |      },
          |      "comments":  {
          |        "count": 2,
          |        "data":  [
          |           {
          |            "created_time": "1348789584",
          |            "text": "@tatianaroso olha o Hiro fofona!!! 😀😋😻",
          |            "from":  {
          |              "username": "cris_kato",
          |              "profile_picture": "http://images.instagram.com/profiles/profile_10818105_75sq_1318383134.jpg",
          |              "id": "10818105",
          |              "full_name": "cris_kato"
          |            },
          |            "id": "289990511869070319"
          |          },
          |           {
          |            "created_time": "1348789993",
          |            "text": "Ahhh não gente!!!! Vou entrar na sua casa e roubar o HIRO prá mim japinha @cris_kato!!! Ele é um goxtosoooo!!!",
          |            "from":  {
          |              "username": "tatianaroso",
          |              "profile_picture": "http://images.instagram.com/profiles/profile_3558556_75sq_1344426261.jpg",
          |              "id": "3558556",
          |              "full_name": "tatianaroso"
          |            },
          |            "id": "289993944546183225"
          |          }
          |        ]
          |      },
          |      "filter": "Normal",
          |      "created_time": "1348789406",
          |      "link": "http://instagr.am/p/QGP3k7MIvT/",
          |      "likes":  {
          |        "count": 14,
          |        "data":  [
          |           {
          |            "username": "ramnit20",
          |            "profile_picture": "http://images.instagram.com/profiles/profile_32677763_75sq_1337131454.jpg",
          |            "id": "32677763",
          |            "full_name": "Tinmar Hernandez"
          |          },
          |           {
          |            "username": "out_to_lunch",
          |            "profile_picture": "http://images.instagram.com/profiles/profile_23092230_75sq_1347209646.jpg",
          |            "id": "23092230",
          |            "full_name": "Esme Salinger"
          |          },
          |           {
          |            "username": "rpmckay",
          |            "profile_picture": "http://images.instagram.com/profiles/profile_14325097_75sq_1340697502.jpg",
          |            "id": "14325097",
          |            "full_name": "Ryan"
          |          },
          |           {
          |            "username": "chefpandita",
          |            "profile_picture": "http://images.instagram.com/profiles/profile_39341_75sq_1348659110.jpg",
          |            "id": "39341",
          |            "full_name": "🐼Yuri @chefpandita"
          |          },
          |           {
          |            "username": "jacobmurphy89",
          |            "profile_picture": "http://images.instagram.com/profiles/profile_175515982_75sq_1338347408.jpg",
          |            "id": "175515982",
          |            "full_name": "Jacob Murphy"
          |          },
          |           {
          |            "username": "freddynava",
          |            "profile_picture": "http://images.instagram.com/profiles/profile_5337743_75sq_1314976548.jpg",
          |            "id": "5337743",
          |            "full_name": "Freddy Nava 💂®"
          |          },
          |           {
          |            "username": "sumi_sunshine",
          |            "profile_picture": "http://images.instagram.com/profiles/profile_34876082_75sq_1338785664.jpg",
          |            "id": "34876082",
          |            "full_name": "Summer Ferrier"
          |          },
          |           {
          |            "username": "shesfromaway",
          |            "profile_picture": "http://images.instagram.com/profiles/profile_2272826_75sq_1346685630.jpg",
          |            "id": "2272826",
          |            "full_name": "ShesFromAway"
          |          },
          |           {
          |            "username": "kimmcash13",
          |            "profile_picture": "http://images.instagram.com/profiles/profile_4446299_75sq_1338311221.jpg",
          |            "id": "4446299",
          |            "full_name": "Kim Cash"
          |          },
          |           {
          |            "username": "tatianaroso",
          |            "profile_picture": "http://images.instagram.com/profiles/profile_3558556_75sq_1344426261.jpg",
          |            "id": "3558556",
          |            "full_name": "tatianaroso"
          |          }
          |        ]
          |      },
          |      "images":  {
          |        "low_resolution":  {
          |          "url": "http://distilleryimage3.s3.amazonaws.com/21e31b7808fd11e2877122000a1fbc4f_6.jpg",
          |          "width": 306,
          |          "height": 306
          |        },
          |        "thumbnail":  {
          |          "url": "http://distilleryimage3.s3.amazonaws.com/21e31b7808fd11e2877122000a1fbc4f_5.jpg",
          |          "width": 150,
          |          "height": 150
          |        },
          |        "standard_resolution":  {
          |          "url": "http://distilleryimage3.s3.amazonaws.com/21e31b7808fd11e2877122000a1fbc4f_7.jpg",
          |          "width": 612,
          |          "height": 612
          |        }
          |      },
          |      "caption":  {
          |        "created_time": "1348789442",
          |        "text": "Hiro!!! #cat #cats #gato #neko",
          |        "from":  {
          |          "username": "cris_kato",
          |          "profile_picture": "http://images.instagram.com/profiles/profile_10818105_75sq_1318383134.jpg",
          |          "id": "10818105",
          |          "full_name": "cris_kato"
          |        },
          |        "id": "289989319545883598"
          |      },
          |      "user_has_liked": false,
          |      "id": "289989015928605651_10818105",
          |      "user":  {
          |        "username": "cris_kato",
          |        "website": "",
          |        "bio": "Walk on!!!! ",
          |        "profile_picture": "http://images.instagram.com/profiles/profile_10818105_75sq_1318383134.jpg",
          |        "full_name": "cris_kato",
          |        "id": "10818105"
          |      }
          |    }
          |  ]
          |}
          |
        """.stripMargin
*/
      val result = (new Instagram.Client("dummy")).users_self_feed

      println(result)
      result mustNotBe null
    }
  }
}
