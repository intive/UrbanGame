
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
import com.github.nscala_time.time.Imports._

case class User(id: Int, login: String)
case class GameSummary(
    gid: Int, version: Int, name: String, 
    startTime: DateTime, endTime: DateTime,
    difficulty: String, 
    operatorName: String, operatorLogo: String,
    maxPlayers: Int, numberOfPlayers: Int,
    awards: String,
    distance: Option[Double]
)
case class GameStatic(
    gid: Int, version: Int, name: String, description: String,
    location: String,
    operatorName: String,
    operatorLogo: String,
    created: DateTime, updated: DateTime,
    startTime: DateTime, endTime: DateTime,
    winning: String, nWins: Int,
    difficulty: String,
    maxPlayers: Int,
    awards: String,
    status: String,
    image: String
)
case class GameDynamic(
    gid: Int, version: Int, status: String, numberOfPlayers: Int
)
case class TaskSummary(
    gid: Int, tid: Int, version: Int,  name: String
)
case class ABCOption(option: String, answer: String)
case class TaskStatic(
    gid: Int, tid: Int, version: Int,
    category: String, name: String, description: String,
    choices: Option[List[ABCOption]],
    maxpoints: Int,
    maxattempts: Int
)
case class TaskDynamic(
    gid: Int, tid: Int, version: Int
)
case class UserGameSummary(
  gid: Int, version: Int, started: DateTime, finished: Option[DateTime], points: Int
)
case class UserGameStatus(points: Int, rank: Int)
case class UserTaskSummary(
  tid: Int, gid: Int, version: Int, t: String, deadline: DateTime
)
case class UserTaskStatus(status: String, attempts: Int, points: Int, canRepeat: Boolean)
case class UserAnswer(lat: Option[Double], lon: Option[Double], option: Option[String])

trait GamesService {
  def listGames(lat: Double, lon: Double): List[GameSummary]
  def getGameStatic(gid: Int): GameStatic
  def getGameDynamic(gid: Int): GameDynamic
  def getTaskStatic(gid: Int, tid: Int): TaskStatic
  def getTaskDynamic(gid: Int, tid: Int): TaskDynamic
  def getUserOpt(login: String, password: String): Option[User]
  def findUserByLoginOpt(login: String): Option[User]
  def createUser(login: String, password: String)
  def joinGame(user: User, gid: Int)
  def leaveGame(user: User, gid: Int)
  def listTasks(user:User, gid: Int, lat: Double, lon: Double): List[TaskSummary]
  def listUserGames(user: User): List[UserGameSummary]
  def getUserGameStatus(user: User, gid: Int): UserGameStatus
  def getUserTaskStatus(user: User, gid: Int, tid: Int): UserTaskStatus
  def checkUserAnswer(user: User, gid: Int, tid: Int, ans: UserAnswer): UserTaskStatus
}

trait UserAuth {
  def authorize[A](request: Request[A]): User
  def apply[A](request: Request[A]): User = authorize(request)
}

class ApiException(code: Int, message: String, params: Seq[Any] = Seq()) extends RuntimeException(message) {
  def getCode = code
  def getParams = params
}

class AuthException(msg: String, params: Seq[Any] = Seq()) extends ApiException(401, msg, params)

