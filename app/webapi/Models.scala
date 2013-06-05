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
    difficulty: String, operatorName: String
)
case class GameStatic(
    gid: Int, version: Int, name: String, description: String,
    location: String,
    operatorName: String,
    created: DateTime, updated: DateTime,
    startTime: DateTime, endTime: DateTime,
    started: Option[DateTime], ended: Option[DateTime],
    winning: String, nWins: Int,
    difficulty: String,
    maxPlayers: Int,
    awards: String,
    status: String,
    image: String
)
case class GameDynamic(gid: Int, version: Int, status: String, free: Int)
case class TaskSummary(gid: Int, tid: Int, version: Int,  name: String)
case class TaskStatic(
    gid: Int, tid: Int, version: Int,
    name: String, description: String,
    deadline: DateTime,
    maxpoints: Int,
    maxattempts: Int
);
case class TaskDynamic(gid: Int, tid: Int, version: Int)
case class UserGameSummary(
  gid: Int, version: Int, started: DateTime, finished: Option[DateTime], points: Int
)
case class UserGameStatus(gid: Int, points: Int)
case class UserTaskStatus(gid: Int, tid: Int, points: Int)

abstract class UserAnswer
case class GPSAnswer(lat: Double, lon: Double) extends UserAnswer
case class ABCAnswer(checked: Int) extends UserAnswer

trait GamesService {
  def listGames(lat: Double, lon: Double, r: Double): List[GameSummary]
  def getGameStatic(gid: Int): GameStatic
  def getGameDynamic(gid: Int): GameDynamic
  def listTasks(gid: Int): List[TaskSummary]
  def getTaskStatic(gid: Int, tid: Int): TaskStatic
  def getTaskDynamic(gid: Int, tid: Int): TaskDynamic
  def getUserOpt(login: String, password: String): Option[User]
  def findUserOpt(login: String): Option[User]
  def createUser(login: String, password: String)
  def joinGame(user: User, gid: Int)
  def leaveGame(user: User, gid: Int)
  def listUserGames(user: User): List[UserGameSummary]
  def getUserGameStatus(user: User, gid: Int): UserGameStatus
  def getUserTaskStatus(user: User, gid: Int, tid: Int): UserTaskStatus
  def checkUserAnswer(user: User, gid: Int, tid: Int, ans: UserAnswer)
}

trait UserAuth {
  def authorize[A](request: Request[A]): User
  def apply[A](request: Request[A]): User = authorize(request)
}

class ApiException(code: Int, message: String) extends RuntimeException(message) {
  def getCode = code
}

class AuthException(msg: String) extends ApiException(401, msg)

