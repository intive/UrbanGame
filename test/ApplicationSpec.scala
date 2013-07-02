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

import play.api.mvc._
import play.api.test._
import play.api.test.Helpers._
import play.api.libs.json._
import scala.slick.session.Database
import play.api.db.slick.Config.driver.simple._
import play.api.Play.current
import models.utils._
import models.dal.Bridges._
import controllers.AuthConfigImpl
import jp.t2v.lab.play2.auth.test.Helpers._

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
class ApplicationSpec extends Specification {

  object config extends AuthConfigImpl
  
  "Application" should {
    
    "send 404 on a bad request" in {
      running(FakeApplication()) {
        route(FakeRequest(GET, "/boum")) must beNone        
      }
    }
    
    "render the index page" in {
      running(FakeApplication()) {
        val home = route(FakeRequest(GET, "/")).get
        
        Thread.sleep(20 * 1000)
        status(home) must equalTo(OK)
        contentType(home) must beSome.which(_ == "text/html")
        contentAsString(home) must contain ("Last created games")
      }
    }
    
    "render the 'my games' page for logged user" in {
      running(FakeApplication()) {
        val mgames = route(FakeRequest(GET, "/my/games").withLoggedIn(config)(1)).get
        
        Thread.sleep(10 * 1000)
        status(mgames) must equalTo(OK)
        contentType(mgames) must beSome.which(_ == "text/html")
        contentAsString(mgames) must contain ("Game list")
      }
    }
    
    "render the 'create new game' page for logged user" in {
      running(FakeApplication()) {
        val ngame = route(FakeRequest(GET, "/my/games/new").withLoggedIn(config)(1)).get
        
        Thread.sleep(10 * 1000)
        status(ngame) must equalTo(OK)
        contentType(ngame) must beSome.which(_ == "text/html")
        contentAsString(ngame) must contain ("Complete the basic data")
      }
    }
    
    "render the 'archive' page for logged user" in {
      running(FakeApplication()) {
        val archive = route(FakeRequest(GET, "/my/games/archive").withLoggedIn(config)(1)).get
        
        Thread.sleep(10 * 1000)
        status(archive) must equalTo(OK)
        contentAsString(archive) must contain ("Archive")
      }
    }
    
    "render the 'options' page for logged user" in {
      running(FakeApplication()) {
        val ngame = route(FakeRequest(GET, "/my/options").withLoggedIn(config)(1)).get
        
        Thread.sleep(10 * 1000)
        status(ngame) must equalTo(OK)
        contentAsString(ngame) must contain ("Options")
      }
    }
    
    "render notification page with a proper flashing message" in {
      running(FakeApplication()) {
        val notif = route(FakeRequest(GET, "/notify").withFlash(("notification", "Test message"))).get
        
        Thread.sleep(10 * 1000)
        status(notif) must equalTo(OK)
        contentAsString(notif) must contain ("Test message")
      }
    }
    
    "confirm given token when data is valid" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {

        import scala.util.{ Try, Success, Failure }
        import java.util._

        val gid: Try[Int] = play.api.db.slick.DB.withSession { implicit session =>
          Operators.create(Operator(id = None, email = "test@test.pl", password = "test", 
            name = "Test1", permission = NormalUser, token = None))
        }
        val token = UUID.randomUUID.toString
        updateSignUpToken("test@test.pl", Some(token))

        val notif = route(FakeRequest(GET, "/confirm/test@test.pl/" + token)).get
        
        Thread.sleep(10 * 1000)
        status(notif) must equalTo(OK)
        contentAsString(notif) must contain ("Your e-mail address has been confirmed. Your account is now ready to use.")
      }
    }
    
    /*
     "fill database with example data" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        val fill = route(FakeRequest(GET, "/filldb")).get

        status(fill) must equalTo(OK)
        contentAsString(fill) must contain ("Inserted 11 game(s) and 2 operator(s) and 1 task(s)")
      }
    }*/

     "send an id in Json object when saving the game" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {

        import scala.util.{ Try, Success, Failure }
        val gid: Try[Int] = play.api.db.slick.DB.withSession { implicit session =>
          Operators.create(Operator(id = None, email = "test@test.pl", password = "test", 
      name = "Test1", permission = NormalUser, token = None))
        }

        val json: JsValue = Json.parse("""
        { 
          "data": {
            "name" : "gametest1",
            "version" : 1,
            "description" : "gametest desc",
            "location" : "somewhere",
            "startTime" : "20:20",
            "startDate" : "2013-05-10",
            "endTime" : "10:10",
            "endDate" : "2013-05-30",
            "winning" : "shortest_time",
            "winningNum" : 2,
            "diff" : "medium",
            "playersNum" : 300,
            "awards" : "some awards",
            "status" : "project",
            "tasksNo" : 2
          } 
        }
        """)
        val gameid = route(FakeRequest(POST, "/my/games/json").withJsonBody(json).withLoggedIn(config)(1)).get
        
        status(gameid) must equalTo(OK)
        contentAsString(gameid) must contain ("val")
      }
    }
	
  }
}
