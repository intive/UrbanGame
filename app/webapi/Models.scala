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
package webapi.models

import play.api._
import play.api.mvc._

case class User(id: Int)
case class GameSummary(gid: Int, name: String)
case class GameStatic (gid: Int, name: String)
case class GameDynamic(gid: Int, version: Int)
case class GameStatus (gid: Int, points: Int)
case class TaskStatus (gid: Int, tid: Int, points: Int)

trait GamesService {
  def listGames(lat: Double, lon: Double, r: Double): List[GameSummary]
  def gameStatic(gid: Int): Option[GameStatic]
  def gameDynamic(gid: Int): Option[GameDynamic]
  def getUser(login: String, password: String): Option[User]
  def createUser(login: String, password: String)
  def listUserGames(user: User): List[GameSummary]
  def getUserGameStatus(user: User, gid: Int): Option[GameStatus]
  def getUserTaskStatus(user: User, gid: Int, tid: Int): Option[TaskStatus]
}

trait UserAuth {
  def authorize[A](request: Request[A]): User
  def apply[A](request: Request[A]): User = authorize(request)
}

class ApiException(code: Int, message: String) extends RuntimeException(message) {
  def getCode = code
}

class AuthException(msg: String) extends ApiException(401, msg)

