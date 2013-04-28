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
import play.api._
import play.api.mvc._
import webapi._
import webapi.models._
import controllers._

trait MainApiModule extends ControllerCache {
  lazy val gamesService: GamesService = ???
  lazy val userAuth: UserAuth = new impl.UserBasicAuth(gamesService)
  val webapiController = registerController(new WebApi(userAuth, gamesService))
}

trait TestApiModule extends MainApiModule {
  override lazy val gamesService: GamesService = new impl.GamesServiceMock
}

object MainApiModule extends MainApiModule
object TestApiModule extends TestApiModule

object Global extends PlayControllerWiring {
  import play.api.Play.current

  override lazy val module = if (Play.isTest)
    TestApiModule
  else if (Play.isDev)
    TestApiModule
  else
    MainApiModule
}
