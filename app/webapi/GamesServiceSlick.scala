
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
  import models.{Games, Tasks, Users, Operators, UserGames, GPSTasks, ABCTasks, UserTasks}
  import play.api.Play.current
  import play.api.db.slick.Config.driver.simple._
  import scala.language.postfixOps

  override def listGames(lat: Double, lon: Double): List[GameSummary] = {
    db withSession { implicit session =>
      val q = for (
        (g, o, d) <- GamesViewWithDistances(lat, lon)
      ) yield (
        g.id, g.version, g.name, 
        g.startTime, g.endTime, 
        g.difficulty, 
        o.email, o.logo,
        g.maxPlayers, g.numberOfPlayers,
        g.awards,
        d
      )

      q.sortBy(_._12).elements map { GameSummary.tupled(_) } toList
    }
  }

  override def getGameStatic(gid: Int): GameStatic = {
    db withSession { implicit session =>
      val q = for (
        (g, o) <- GamesView if g.id === gid
      ) yield (
        g.id, g.version, g.name, g.description, g.location,
        o.email, o.logo,
        g.created, g.updated, g.startTime, g.endTime,
        g.winning, g.nWins, g.difficulty, g.maxPlayers, g.awards,
        g.status, g.image
      )

      q.firstOption map { GameStatic.tupled(_) } getOrElse { throw gameNotFound }
    }
  }

  override def getGameDynamic(gid: Int): GameDynamic = {
    db withSession { implicit session =>
      val q = for (
        g <- PublicGamesView if g.id === gid
      ) yield (g.id, g.version, g.status, g.numberOfPlayers)

      q.firstOption map { GameDynamic.tupled(_) } getOrElse { throw gameNotFound }
    }
  }

  def isABCTask(category: String) = category contains "ABC"

  override def getTaskStatic(gid: Int, tid: Int): TaskStatic = {
    db withSession { implicit session =>
      val q = for (
        t <- Tasks if t.gameId === gid && t.id === tid
      ) yield (
        t.gameId, t.id, t.version, t.category, t.name, t.description,
        t.maxpoints, t.maxattempts
      )

      q.firstOption map { case (gid, tid, version, category, name, description, maxpoints, maxattempts) =>
        val choices = 
          if (category contains "ABC") 
            Some((for (t <- ABCTasks if t.gameId === gid && t.taskId === tid) yield(t.char, t.option)).elements map { ABCOption.tupled(_) } toList)
          else 
            None

        TaskStatic(gid, tid, version, category, name, description, choices, maxpoints, maxattempts)
      } getOrElse { throw taskNotFound }
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

  override def findUserByLoginOpt(login: String): Option[User] = {
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
    findUserByLoginOpt(login) map { exists => throw userExists } getOrElse {
      db withSession { implicit session =>
        Users insert models.User(None, login, hash(login,password))
      }
    }
  }
  
  override def listUserGames(user: User): List[UserGameSummary] = {
    db withSession { implicit session =>
      val q = for (
        (ug, g) <- UserGamesView(user.id)
      ) yield (g.id, g.version, ug.joined, ug.left, ug.points)

      q.elements map { UserGameSummary.tupled(_) } toList
    }
  }

  override def joinGame(user: User, gid: Int) {
    db withSession { implicit session =>
      val gameInfo = for (
        g <- Games if g.id === gid
      ) yield (g.status, g.maxPlayers, g.numberOfPlayers)

      gameInfo.firstOption map { case (status, maxPlayers, numOfPlayers) =>
        if (status == "ended")
          throw gameIsEnded
        if (status != "online")
          throw gameNotStarted
        if (maxPlayers == numOfPlayers)
          throw gameIsFull

        val userJoinedInfo = for (
          ug <- UserGames if ug.userId === user.id && ug.gameId === gid
        ) yield (ug.joined ~ ug.left)
        userJoinedInfo.firstOption match { 
          case Some((joined, left)) =>
            if (left.isEmpty) 
              throw alreadyInGame

            val now = DateTime.now
            val wait = 30 - (left.get to now).millis / (60*1000)
            if (wait > 0) 
              throw userMustWait(wait)

            userJoinedInfo update (now, None)

          case None =>
            UserGames insert models.UserGame(user.id, gid, DateTime.now, None, 0)
        }

      } getOrElse (throw gameNotFound)

      updateNumberOfPlayers(gid, +1)
    }
  }

  override def leaveGame(user: User, gid: Int) {
    db withSession { implicit session =>
      val leftDate = for (
        ug <- UserGames if ug.userId === user.id && ug.gameId === gid
      ) yield ug.left

      leftDate.firstOption map { left =>
        if (left.isDefined) 
          throw alreadyLeft

        leftDate update Some(DateTime.now)
        updateNumberOfPlayers(gid, -1)

      } getOrElse (throw notPartOfGame)
    }
  }

  def canShowTask(t: models.Tasks.type, user: User, lat: Double, lon: Double) =
    t.cancelled || (for (g <- Games if g.id === t.gameId && (
      g.status === "ended" || 
      g.status === "online" && 
      (t.rangeLimit.isNull || geodistance(t.lat, t.lon, lat, lon) < t.rangeLimit) && 
      (t.timeLimit.isNull || t.timeLimit < DateTime.now)
    )) yield g).exists

  override def listTasks(user: User, gid: Int, lat: Double, lon: Double): List[TaskSummary] = {
    db withSession { implicit session =>
      val q = for (
        t <- Tasks if t.gameId === gid
        if canShowTask(t, user, lat, lon)
      ) yield (t.gameId, t.id, t.version, t.name)

      q.elements map { TaskSummary.tupled(_) } toList
    }
  }

  def updateNumberOfPlayers(gid: Int, a: Int)(implicit session: Session) {
    val q = for (g <- Games if g.id === gid) yield g.numberOfPlayers
    val num = q.first
    q update (num+a)
  }
  
  override def getUserGameStatus(user: User, gid: Int) = {
    db withSession { implicit session =>
      val q = for (
        ug <- UserGames if ug.userId === user.id && ug.gameId === gid
      ) yield (ug.points, 0)
      
      q.firstOption map { UserGameStatus.tupled(_) } getOrElse (throw notPartOfGame)
    }
  }

  override def getUserTaskStatus(user: User, gid: Int, tid: Int) = {
    db withSession { implicit session =>
      val now = DateTime.now
      val q = for (
        (ut, t) <- UserTasksView(user.id, gid, tid)
      ) yield (ut.status, ut.attempts, ut.points, ut.attempts < t.maxattempts)

      q.firstOption map { UserTaskStatus.tupled(_) } getOrElse (throw taskNotFound)
    }
  }

  override def checkUserAnswer(user: User, gid: Int, tid: Int, ans: UserAnswer) = {
  }

  val geodistanceFun = SimpleFunction[Double]("geodistance")
  def geodistance(lat1: Column[Option[Double]], lon1: Column[Option[Double]], lat2: Double, lon2: Double) =
    geodistanceFun(Seq(lat1, lon1, lat2, lon2))

  val PublicGamesView = 
    Games filter (g => g.status === "published" || g.status === "online")
  val GamesView =
    PublicGamesView innerJoin Operators on (_.operatorId === _.id)
  def GamesViewWithDistances(lat: Double, lon: Double) = 
    GamesView map { case (g, o) =>
      (g, o, (for (t <- GPSTasks if t.gameId === g.id) yield geodistance(t.lat, t.lon, lat, lon)).min)
    }
  def UserGamesView(uid: Int) =
    UserGames filter (_.userId === uid) innerJoin Games on (_.gameId === _.id)
  def UserTasksView(uid: Int, gid: Int, tid: Int) =
    UserTasks filter (ut => ut.userId === uid && ut.gameId === gid && ut.taskId === tid) innerJoin Tasks on ((ut, t) => ut.gameId === t.gameId && ut.taskId === t.id)

  val sha256 = java.security.MessageDigest.getInstance("SHA-256")
  def hash(login: String, password: String) = {
    sha256.digest((login+":"+password).getBytes("UTF-8")) map { "%02X" format _ } mkString
  }


  import play.api.i18n._
  val msgPre = "webapi.error."
  val gameNotFound   = new ApiException(404, Messages(msgPre+"gameNotFound"))
  val taskNotFound   = new ApiException(404, Messages(msgPre+"taskNotFound"))
  val userExists     = new ApiException(409, Messages(msgPre+"userExists"))
  val notPartOfGame  = new ApiException(404, Messages(msgPre+"notPartOfGame"))
  val gameNotStarted = new ApiException(400, Messages(msgPre+"notStarted"))
  val gameIsEnded    = new ApiException(400, Messages(msgPre+"isEnded"))
  val alreadyInGame  = new ApiException(400, Messages(msgPre+"alreadyJoined"))
  val gameIsFull     = new ApiException(400, Messages(msgPre+"isFull"))
  val alreadyLeft    = new ApiException(400, Messages(msgPre+"alreadyLeft"))
  def userMustWait(m: Long) = new ApiException(400, Messages(msgPre+"mustWait", m))
}

