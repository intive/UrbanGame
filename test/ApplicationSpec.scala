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

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
class ApplicationSpec extends Specification {
  
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
    
    "render the 'my games' page" in {
      running(FakeApplication()) {
        val mgames = route(FakeRequest(GET, "/my/games")).get
        
        Thread.sleep(10 * 1000)
        status(mgames) must equalTo(OK)
        contentType(mgames) must beSome.which(_ == "text/html")
        contentAsString(mgames) must contain ("Game list")
      }
    }
    
    "render the 'create new game' page" in {
      running(FakeApplication()) {
        val ngame = route(FakeRequest(GET, "/my/games/new")).get
        
        Thread.sleep(10 * 1000)
        status(ngame) must equalTo(OK)
        contentType(ngame) must beSome.which(_ == "text/html")
        contentAsString(ngame) must contain ("Step 1")
      }
    }
    
    "render the 'archive' page" in {
      running(FakeApplication()) {
        val archive = route(FakeRequest(GET, "/my/games/archive")).get
        
        Thread.sleep(10 * 1000)
        status(archive) must equalTo(OK)
        contentAsString(archive) must contain ("Archive")
      }
    }
    
    "render the 'options' page" in {
      running(FakeApplication()) {
        val ngame = route(FakeRequest(GET, "/my/options")).get
        
        Thread.sleep(10 * 1000)
        status(ngame) must equalTo(OK)
        contentAsString(ngame) must contain ("Options")
      }
    }
    
    "send an id in Json object when saving the game" in {
      running(FakeApplication()) {
        val json: JsValue = Json.parse("""
        { 
          "game": {
            "name" : "gametest1",
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
            "awards" : "some awards"
          } 
        }
        """)
        val gameid = route(FakeRequest(POST, "/my/games").withJsonBody(json)).get
        
        Thread.sleep(10 * 1000)
        status(gameid) must equalTo(OK)
        contentAsString(gameid) must contain ("id")
      }
    }
	
  }
}
