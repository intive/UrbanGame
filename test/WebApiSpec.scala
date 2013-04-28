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

class WebApiSpec extends Specification {
  
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
        val games = route(FakeRequest(GET, "/api/games")).get
        
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
    val existingUser = (
      ("Authorization", "Basic "+Base64.encodeBase64String("user:pass" getBytes))
    )
    val nonexistingUser = (
      ("Authorization", "Basic "+Base64.encodeBase64String("XXX:pass" getBytes))
    )

    "send UNAUTHORIZED status for non-existing user on 'login' command" in {
      running(FakeApplication()) {
        val login = route(FakeRequest(GET, "/api/login").withHeaders(nonexistingUser)).get
        
        status(login) must equalTo(UNAUTHORIZED)
      }
    }

    "send OK status for existing user on 'login' command" in {
      running(FakeApplication()) {
        val login = route(FakeRequest(GET, "/api/login").withHeaders(existingUser)).get
        
        status(login) must equalTo(OK)
      }
    }

    "send game info for logged user in Json format" in {
      running(FakeApplication()) {
        val game = route(FakeRequest(GET, "/api/games/42/dynamic").withHeaders(existingUser)).get
        
        status(game) must equalTo(OK)
        contentType(game) must beOneOf (Some("text/json"), Some("application/json"), Some("application/hal+json"))
      }
    }
  }
}
