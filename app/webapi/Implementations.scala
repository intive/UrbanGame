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
package webapi.impl

import play.api._
import play.api.mvc._
import webapi.models._

class GamesServiceMock extends GamesService {
  override def listGames(lat: Double, lon: Double, r: Double) = list
  override def getGameStatic(gid: Int) = static get gid getOrElse {throw new ApiException(404,  "Game not found")}
  override def getGameDynamic(gid: Int) = dynamic get gid getOrElse {throw new ApiException(404,  "Game not found")}
  override def listTasks(gid: Int) = tasks get gid getOrElse {throw new ApiException(404, "Task not found")}
  override def getTaskStatic(gid: Int, tid: Int) = taskStatic get (gid,tid) getOrElse {throw new ApiException(404,  "Game not found")}
  override def getTaskDynamic(gid: Int, tid: Int) = taskDynamic get (gid,tid) getOrElse {throw new ApiException(404,  "Game not found")}
  override def getUserOpt(user: String, hash: String) = users get user    
  override def createUser(user: String, hash: String) { users += (user -> User(hash.hashCode)) }
  override def listUserGames(user: User) = list
  override def getUserGameStatus(user: User, gid: Int) = UserGameStatus(gid, 123)
  override def getUserTaskStatus(user: User, gid: Int, tid: Int) = UserTaskStatus(gid, tid, 123)
  override def joinGame(user: User, gid: Int) = {} 
  override def leaveGame(user: User, gid: Int) = {}
  override def checkUserAnswer(user: User, gid: Int, tid: Int, ans: UserAnswer) = {}
  
  private val list = List(
    GameSummary(123, "dasdsd"),
    GameSummary(42, "dasdsd"),
    GameSummary(564, "dasdsd")
  )

  private val static = Map(
    123 -> GameStatic(123, "dasdsd"),
    42  -> GameStatic(42, "dasdsd"),
    564 -> GameStatic(564, "dasdsd")
  )

  private val dynamic = Map(
    123 -> GameDynamic(123, 0),
    42  -> GameDynamic( 42, 1),
    564 -> GameDynamic(564, 2)
  )

  private val tasks = Map(
    123 -> List(TaskSummary(123,0,"fdsf"), TaskSummary(123,1,"fffff")),
    42  -> List(TaskSummary( 42,0,"fdsf"), TaskSummary( 42,1,"fffff")),
    564 -> List(TaskSummary(564,0,"fdsf"), TaskSummary(564,1,"fffff"))
  )

  private val taskStatic = Map(
    (123, 0) -> TaskStatic(123,0,"fdsf"),
    (123, 1) -> TaskStatic(123,1,"fffff"),
    ( 42, 0) -> TaskStatic( 42,0,"fdsf"),
    ( 42, 1) -> TaskStatic( 42,1,"fffff"),
    (564, 0) -> TaskStatic( 42,0,"fdsf"),
    (564, 1) -> TaskStatic( 42,1,"fffff")
  )

  private val taskDynamic = Map(
    (123, 0) -> TaskDynamic(123,0,1),
    (123, 1) -> TaskDynamic(123,1,1),
    ( 42, 0) -> TaskDynamic( 42,0,1),
    ( 42, 1) -> TaskDynamic( 42,1,1),
    (564, 0) -> TaskDynamic( 42,0,1),
    (564, 1) -> TaskDynamic( 42,1,1)
  )

  import scala.collection.mutable.Map
  private val users = Map(
    "admin" -> User(0),
    "user" -> User(666)
  )
}

class UserBasicAuth(gamesService: GamesService) extends UserAuth {
  
  override def authorize[A](request: Request[A]): User = {
    val header = getAuthHeader(request)
    val (login, hash) = parseHeader(header)
    gamesService.getUserOpt(login, hash) getOrElse { 
      throw new AuthException("User not found")
    }
  }
 
  import org.apache.commons.codec.binary.Base64
  import scala.util.matching.Regex

  private def getAuthHeader[A](request: Request[A]): String =
    request.headers.get("Authorization") getOrElse { 
      throw new AuthException("No Auth header!")
    }

  private def decode(encoded: String):Option[String] = {
    def isLegalChar(c: Char) = (c >= ' ' && c <= '~')
    val res = new String(Base64.decodeBase64(encoded))
    if (res forall isLegalChar) Some(res) else None
  } 
  
  private def parseHeader(header: String): (String,String) = {
    val pattern = new Regex("""Basic ([\w\d\+/=]+)""", "base64")
    val res = for {
      matched <- pattern findFirstMatchIn header
      decoded <- decode(matched group ("base64"))
      (login, hash) <- Some(decoded span {_ != ':'})
    } yield (login, hash drop 1)

    res getOrElse { throw new AuthException("Invalid Auth header!") }
  }
}
