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

class GamesServiceSlick(db: play.api.db.slick.DB) extends GamesService {
  import com.github.tototoshi.slick.JodaSupport._
  import com.github.nscala_time.time.Imports._
  import models.{Games, Tasks, Users, Operators, UserGames}
  import play.api.Play.current
  import play.api.db.slick.Config.driver.simple._
  import scala.language.postfixOps

  override def listGames(lat: Double, lon: Double, r: Double): List[GameSummary] = {
    db withSession { implicit session =>
      val q = for (
        (g, o) <- GamesView
        if g.status === "published" || g.status === "online"
      ) yield (g.id, g.version, g.name, g.startTime, g.endTime, g.difficulty, o.login)

      q.elements map { GameSummary.tupled(_) } toList
    }
  }

  override def getGameStatic(gid: Int): GameStatic = {
    db withSession { implicit session =>
      val q = for (
        (g, o) <- GamesView if g.id === gid
      ) yield (
        g.id, g.version, g.name, g.description, g.location,
        o.login, g.created, g.updated, g.startTime, g.endTime,
        g.started, g.ended, g.winning, g.nWins,
        g.difficulty, g.maxPlayers, g.awards,
        g.status, g.image
      )

      q.firstOption map { GameStatic.tupled(_) } getOrElse { throw gameNotFound }
    }
  }

 override def getGameDynamic(gid: Int): GameDynamic = {
    db withSession { implicit session =>
      val q = for (
        g <- Games if g.id === gid
      ) yield (g.id, g.version, g.status, g.maxPlayers)

      q.firstOption map { GameDynamic.tupled(_) } getOrElse { throw gameNotFound }
    }
  }

  override def listTasks(gid: Int): List[TaskSummary] = {
    db withSession { implicit session =>
      val q = for (
        t <- Tasks if t.gameId === gid
      ) yield (t.gameId, t.id, t.version, t.name)

      q.elements map { TaskSummary.tupled(_) } toList
    }
  }

  override def getTaskStatic(gid: Int, tid: Int): TaskStatic = {
    db withSession { implicit session =>
      val q = for (
        t <- Tasks if t.gameId === gid && t.id === tid
      ) yield (
        t.gameId, t.id, t.version, t.name, t.description, t.deadline, 
        t.maxpoints, t.maxattempts
      )

      q.firstOption map { TaskStatic.tupled(_) } getOrElse { throw taskNotFound }
    }
  }

  override def getTaskDynamic(gid: Int, tid: Int): TaskDynamic = {
    db withSession { implicit session =>
      val q = for (
        t <- Tasks if t.gameId === gid && t.id === tid
      ) yield (t.gameId, t.id, t.version)

      q.firstOption map { TaskDynamic.tupled(_) } getOrElse { throw taskNotFound }
    }
  }

  override def findUserOpt(login: String): Option[User] = {
    db withSession { implicit session =>
      val q = for (
        u <- Users if u.login === login
      ) yield (u.id, u.login)

      q.firstOption map { User.tupled(_) }
    }
  }

  override def getUserOpt(login: String, password: String): Option[User] = {
    db withSession { implicit session =>
      val q = for (
        u <- Users if u.login === login && u.hash === hash(login,password)
      ) yield (u.id, u.login)

      q.firstOption map { User.tupled(_) }
    }
  }
  
  override def createUser(login: String, password: String) { 
    findUserOpt(login) map { exists => throw userExists } getOrElse {
      db withSession { implicit session =>
        Users insert models.User(None, login, hash(login,password))
      }
    }
  }
  
  override def listUserGames(user: User): List[UserGameSummary] = {
    db withSession { implicit session =>
      val q = for (
        (ug, g) <- UserGamesView(user.id)
      ) yield (g.id, g.version, ug.joined, ug.left, 0)

      q.elements map { UserGameSummary.tupled(_) } toList
    }
  }

  override def joinGame(user: User, gid: Int) {
    db withSession { implicit session =>
      val test = for (g <- Games if g.id === gid) yield (g.startTime, g.endTime)
      test.firstOption map { case (start, end) =>
        val now = DateTime.now
        if (start > now) throw gameNotStarted
        if (end   < now) throw gameIsEnded
        val test2 = for (ug <- UserGames if ug.userId === user.id && ug.gameId === gid) yield (ug.joined ~ ug.left)
        test2.firstOption match { 
          case Some((joined, left)) =>
            if (left.isEmpty) throw alreadyInGame
            val wait = 30 - (left.get to now).millis / (60*1000)
            if (wait > 0) throw userMustWait(wait)
            test2 update (now, None)
          case None =>
            UserGames insert models.UserGame(user.id, gid, DateTime.now, None)
        }
      } getOrElse (throw gameNotFound)
    }
  }

  override def leaveGame(user: User, gid: Int) {
    db withSession { implicit session =>
      val q = for (
        ug <- UserGames if ug.userId === user.id && ug.gameId === gid
      ) yield ug

      q.firstOption map { ug =>
        if (ug.left.isDefined) throw alreadyLeft
        q update models.UserGame(user.id, gid, ug.joined, Some(DateTime.now))
      } getOrElse (throw notPartOfGame)
    }
  }
  
  override def getUserGameStatus(user: User, gid: Int) = UserGameStatus(gid, 123)
  override def getUserTaskStatus(user: User, gid: Int, tid: Int) = UserTaskStatus(gid, tid, 123)
  override def checkUserAnswer(user: User, gid: Int, tid: Int, ans: UserAnswer) = {}

  val GamesView = Games innerJoin Operators on (_.operatorId === _.id)
  def UserGamesView(uid: Int) =
    UserGames filter (_.userId === uid) innerJoin Games on (_.gameId === _.id) 

  val sha256 = java.security.MessageDigest.getInstance("SHA-256")
  def hash(login: String, password: String) = {
    sha256.digest((login+":"+password).getBytes("UTF-8")) map { "%02X" format _ } mkString
  }


  val gameNotFound   = new ApiException(404, "Game not found")
  val taskNotFound   = new ApiException(404, "Task not found")
  val userExists     = new ApiException(409, "User with this login exists")
  val notPartOfGame  = new ApiException(404, "You are not part of game")
  val gameNotStarted = new ApiException(400, "Game is not started")
  val gameIsEnded    = new ApiException(400, "Game is ended")
  val alreadyInGame  = new ApiException(400, "User is already joined")
  val alreadyLeft    = new ApiException(400, "User already left the game")
  def userMustWait(m: Long) = new ApiException(400, s"User must wait $m minutes")

}

