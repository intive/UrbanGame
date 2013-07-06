/**Copyright 2013 BLStream, BLStream's Patronage Program Contributors
 * 		 http://blstream.github.com/UrbanGame/
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 		 http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package test

import org.specs2.mutable._

import play.api.test._
import play.api.test.Helpers._
import play.api.libs.json._

class WebApiSpec extends Specification {

  //prepare database for tests
  running(FakeApplication()) {
    import play.api.db.slick.DB
    import models.{Users, UserGames}
    import scala.slick.jdbc.{GetResult, StaticQuery => Q}
    import Q.interpolation
    import play.api.Play.current

    DB("urbangameApi") withSession { implicit session =>
      val uq = sql"""SELECT "id" FROM USERS WHERE "login" = 'new_user'""".as[Int] 
      uq.firstOption map { uid =>
        sqlu"""DELETE FROM USERTASKS WHERE "userId" = $uid""".execute
        sqlu"""DELETE FROM USERGAMES WHERE "userId" = $uid""".execute
        sqlu"""DELETE FROM USERS WHERE "id" = $uid""".execute
      }
    }
  }
  
  "WebApi" should {
    
    "send 404 on a bad request" in {
      running(FakeApplication()) {
        route(FakeRequest(GET, "/boum")) must beNone        
      }
    }
    
    "send root(home) resource with OK status" in {
      running(FakeApplication()) {
        val root = route(FakeRequest(GET, "/api")).get
        
        status(root) must equalTo(OK)
      }
    }
	
    "send list of games in Json format" in {
      running(FakeApplication()) {
        val games = route(FakeRequest(GET, "/api/games?lat=0&lon=0")).get
        
        status(games) must equalTo(OK)
        contentType(games) must beOneOf (Some("text/json"), Some("application/json"), Some("application/hal+json"))
      }
    }

    "send UNAUTHORIZED status for non-logged user on 'get game details' command" in {
      running(FakeApplication()) {
        val game = route(FakeRequest(GET, "/api/games/42/static")).get
        
        status(game) must equalTo(UNAUTHORIZED)
        header("WWW-Authenticate", game).get must beMatching("Basic realm=\".*\"")
      }
    }

    import org.apache.commons.codec.binary.Base64
    val newUser = (
      ("Authorization", "Basic "+Base64.encodeBase64String("new_user:pass" getBytes))
    )

    "send UNAUTHORIZED status for loggining non-existing user" in {
      running(FakeApplication()) {
        val login = route(FakeRequest(GET, "/api/login").withHeaders(newUser)).get
        
        status(login) must equalTo(UNAUTHORIZED)
      }
    }
    
    "send OK status after creating new user" in {
      val json = Json.obj("login"->"new_user", "password"->"pass")
      running(FakeApplication()) {
        val register = route(FakeRequest(POST, "/api/register").withJsonBody(json)).get
        status(register) must equalTo(OK)
      }
    }
    
    "send CONFLICT status after register new user with existing login" in {
      val json = Json.obj("login"->"new_user", "password"->"pass2")
      running(FakeApplication()) {
        val register = route(FakeRequest(POST, "/api/register").withJsonBody(json)).get
        status(register) must equalTo(CONFLICT)
      }
    }

    "send game info for logged user in Json format" in {
      running(FakeApplication()) {
        val game = route(FakeRequest(GET, "/api/games/1/dynamic").withHeaders(newUser)).get
        
        status(game) must equalTo(OK)
        contentType(game) must beOneOf (Some("text/json"), Some("application/json"), Some("application/hal+json"))
      }
    }

    "send UNAUTHORIZED status on 'join game' command for non-logged user" in {
      val json = Json.obj()
      running(FakeApplication()) {
        val join = route(FakeRequest(POST, "/api/games/1").withJsonBody(json)).get

        status(join) must equalTo(UNAUTHORIZED)
      }
    }
 
    "send OK status for loggining existing user" in {
      running(FakeApplication()) {
        val login = route(FakeRequest(GET, "/api/login").withHeaders(newUser)).get
        
        status(login) must equalTo(OK)
      }
    }
   
    "send OK status on 'join game' for logged user" in {
      val json = Json.obj()
      running(FakeApplication()) {
        val join1 = route(FakeRequest(POST, "/api/games/1").withHeaders(newUser).withJsonBody(json)).get

        status(join1) must equalTo(OK)
      }
    }
 
    "send BAD_REQUEST status on 'join game' for user already joined" in {
      val json = Json.obj()
      running(FakeApplication()) {
        val join = route(FakeRequest(POST, "/api/games/1").withHeaders(newUser).withJsonBody(json)).get

        status(join) must equalTo(BAD_REQUEST)
      }
    }

    "properly list user games" in {
      running(FakeApplication()) {
        val list = route(FakeRequest(GET, "/api/my/games").withHeaders(newUser)).get

        status(list) must equalTo(OK)
        contentType(list) must beOneOf (Some("text/json"), Some("application/json"), Some("application/hal+json"))
      }
    }

    "properly list tasks" in {
      running(FakeApplication()) {
        val list = route(FakeRequest(GET, "/api/games/1/tasks?lat=1&lon=1").withHeaders(newUser)).get
        status(list) must equalTo(OK)
        contentType(list) must beOneOf (Some("text/json"), Some("application/json"), Some("application/hal+json"))
      }
    }
    
    "display task static information" in {
      running(FakeApplication()) {
        val list = route(FakeRequest(GET, "/api/games/1/tasks/1/static").withHeaders(newUser)).get

        status(list) must equalTo(OK)
        contentType(list) must beOneOf (Some("text/json"), Some("application/json"), Some("application/hal+json"))
      }
    }
 
    "response and reject user solution for ABC task" in {
      running(FakeApplication()) {
        val json = Json.obj("options" -> List("c", "d"))
        val resp = route(FakeRequest(POST, "/api/games/1/tasks/2").withHeaders(newUser).withJsonBody(json)).get

        status(resp) must equalTo(OK)
        contentType(resp) must beOneOf (Some("text/json"), Some("application/json"), Some("application/hal+json"))
        val respJson = Json.parse(contentAsString(resp))
        ((respJson \\ "status")(0)).as[String] must equalTo("rejected")
      }
    }
 
    "response and accept user solution for ABC task" in {
      running(FakeApplication()) {
        val json = Json.obj("options" -> List("a", "b"))
        val resp = route(FakeRequest(POST, "/api/games/1/tasks/2").withHeaders(newUser).withJsonBody(json)).get

        status(resp) must equalTo(OK)
        contentType(resp) must beOneOf (Some("text/json"), Some("application/json"), Some("application/hal+json"))
        val respJson = Json.parse(contentAsString(resp))
        ((respJson \\ "status")(0)).as[String] must equalTo("accepted")
      }
    }
 
    "send BAD REQUEST when user sent answer too many times" in {
      running(FakeApplication()) {
        val json = Json.obj("options" -> List("a", "b"))
        val resp = route(FakeRequest(POST, "/api/games/1/tasks/0").withHeaders(newUser).withJsonBody(json)).get

        status(resp) must equalTo(BAD_REQUEST)
        contentType(resp) must beOneOf (Some("text/json"), Some("application/json"), Some("application/hal+json"))
      }
    }
 
    "response and accept user solution for GPS task" in {
      running(FakeApplication()) {
        val json = Json.obj("lat" -> 51.14212, "lon" -> 16.94169)
        val resp = route(FakeRequest(POST, "/api/games/1/tasks/0").withHeaders(newUser).withJsonBody(json)).get

        status(resp) must equalTo(OK)
        contentType(resp) must beOneOf (Some("text/json"), Some("application/json"), Some("application/hal+json"))
        val respJson = Json.parse(contentAsString(resp))
        ((respJson \\ "status")(0)).as[String] must equalTo("accepted")
      }
    }

    "send OK status after leaving game" in {
      running(FakeApplication()) {
        val del = route(FakeRequest(DELETE, "/api/my/games/1").withHeaders(newUser)).get

        status(del) must equalTo(OK)
      }
    }

    "send BAD_REQUEST status after leaving game again" in {
      running(FakeApplication()) {
        val del = route(FakeRequest(DELETE, "/api/my/games/1").withHeaders(newUser)).get

        status(del) must equalTo(BAD_REQUEST)
      }
    }
    
    "send BAD_REQUEST status when user try to join to game again after less than 30 min" in {
      running(FakeApplication()) {
        val del = route(FakeRequest(DELETE, "/api/my/games/1").withHeaders(newUser)).get

        status(del) must equalTo(BAD_REQUEST)
      }
    }
  }
}
