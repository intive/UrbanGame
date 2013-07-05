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
  import models.utils._
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
        d,
        g.image
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

  override def getTaskStatic(gid: Int, tid: Int): TaskStatic = {
    def getListOfABCAnswers(gid: Int, tid: Int)(implicit session: Session) = {
      val q = for (t <- models.ABCTasks if t.gameId === gid && t.taskId === tid) yield(t.char, t.option)
      q.elements map { ABCOption.tupled(_) } toList
    }
      
    db withSession { implicit session =>
      val q = for (
        t <- models.Tasks if t.gameId === gid && t.id === tid
      ) yield (
        t.gameId, t.id, t.version, t.ttype, t.name, t.description,
        t.maxpoints, t.maxattempts
      )

      q.firstOption map { case (gid, tid, version, ttype, name, description, maxpoints, maxattempts) =>
        val choices = if (isABCTask(ttype)) Some(getListOfABCAnswers(gid, tid)) else None
        TaskStatic(gid, tid, version, ttype, name, description, choices, maxpoints, maxattempts)
      } getOrElse { throw taskNotFound }
    }
  }

  override def getTaskDynamic(gid: Int, tid: Int): TaskDynamic = {
    db withSession { implicit session =>
      val q = for (
        t <- models.Tasks if t.gameId === gid && t.id === tid
      ) yield (t.gameId, t.id, t.version)

      q.firstOption map { TaskDynamic.tupled(_) } getOrElse { throw taskNotFound }
    }
  }

  override def findUserByLoginOpt(login: String): Option[User] = {
    db withSession { implicit session =>
      val q = for (
        u <- models.Users if u.login === login
      ) yield (u.id, u.login)

      q.firstOption map { User.tupled(_) }
    }
  }

  override def getUserOpt(login: String, password: String): Option[User] = {
    db withSession { implicit session =>
      val q = for (
        u <- models.Users if u.login === login && u.hash === hash(login,password)
      ) yield (u.id, u.login)

      q.firstOption map { User.tupled(_) }
    }
  }
  
  override def createUser(login: String, password: String) { 
    findUserByLoginOpt(login) map { exists => throw userExists } getOrElse {
      db withSession { implicit session =>
        models.Users insert UserDB(None, login, hash(login,password))
      }
    }
  }

  override def listUserGames(user: User): List[UserGameSummary] = {
    db withSession { implicit session =>
      val q = for (
        ug <- models.UserGames if ug.userId === user.id
      ) yield (ug.gameId, ug.joined, ug.left, ug.points, -1)
        
      q.elements map { UserGameSummary.tupled(_) } toList
    }
  }

  override def joinGame(user: User, gid: Int) {
   val ugq = for (
      ug <- models.UserGames if ug.userId === user.id && ug.gameId === gid
    ) yield (ug.joined ~ ug.left)

   def gameIsValid(fun: => Unit)(implicit session: Session) {
      val gameInfo = for (
        g <- models.Games if g.id === gid
      ) yield (g.status, g.maxPlayers, g.numberOfPlayers)

      gameInfo.firstOption map { case (status, maxPlayers, numOfPlayers) =>
        if (status == "ended") throw gameIsEnded
        if (status != "online") throw gameNotStarted
        if (maxPlayers == numOfPlayers) throw gameIsFull
        fun
      } getOrElse (throw gameNotFound)
    }

    def userGameIsValid(fun: (Boolean) => Unit)(implicit session: Session) {
       ugq.firstOption map { case (joined, left) =>
        if (left.isEmpty) throw alreadyInGame
        val wait = 30 - (left.get to DateTime.now).millis / (60*1000)
        if (wait > 0)  throw userMustWait(wait)
        fun(false)
      } getOrElse fun(true)
    }

    db withSession { implicit session =>
      gameIsValid { userGameIsValid { noRow =>
        val now = DateTime.now
        if (noRow)
          models.UserGames insert UserGame(user.id, gid, now, None, 0)
        else
          ugq update (now, None)
        updateNumberOfPlayers(gid, +1)
      } }
    }
  }

  override def leaveGame(user: User, gid: Int) {
    val ugq = for (
      ug <- models.UserGames if ug.userId === user.id && ug.gameId === gid
    ) yield ug.left

    def userGameIsValid(fun: => Unit)(implicit session: Session) {
      ugq.firstOption map { left =>
        if (left.isDefined) throw alreadyLeft
        fun
      } getOrElse (throw notPartOfGame)
    }

    db withSession { implicit session =>
      userGameIsValid { 
        ugq update Some(DateTime.now)
        updateNumberOfPlayers(gid, -1)
      }
    }
  }

  override def listTasks(user: User, gid: Int, lat: Double, lon: Double): List[TaskSummary] = {
    db withSession { implicit session =>
      val q = for (
        t <- models.Tasks if t.gameId === gid
        if canShowTask(t, user, lat, lon)
      ) yield (t.gameId, t.id, t.version, t.name, t.ttype)

      q.elements map { TaskSummary.tupled(_) } toList
    }
  }

 
  override def getUserGameStatus(user: User, gid: Int) = {
    db withSession { implicit session =>
      val q = for (
        ug <- models.UserGames if ug.userId === user.id && ug.gameId === gid
      ) yield (ug.points, -1)
      
      q.firstOption map { UserGameStatus.tupled(_) } getOrElse (throw notPartOfGame)
    }
  }

  override def getUserTaskStatus(user: User, gid: Int, tid: Int) = {
    db withSession { implicit session =>
      val now = DateTime.now
      val q = for (
        (ut, t) <- UserTasksView(user.id, gid) if t.id === tid
      ) yield (ut.status, ut.attempts, ut.points, ut.attempts < t.maxattempts)

      q.firstOption map { UserTaskStatus.tupled(_) } getOrElse (throw taskNotFound)
    }
  }

  override def checkUserAnswer(user: User, gid: Int, tid: Int, ans: UserAnswer): UserTaskStatus = {
    def userGameIsValid(fun: Int => UserTaskStatus)(implicit session: Session): UserTaskStatus = {
       val ugvq = for ((ug, g) <- UserGamesView(user.id) if ug.left.isNull) yield (g.status, ug.points)

       ugvq.firstOption map { case (status, gamePoints) =>
        if (status == "ended")  throw gameIsEnded
        if (status != "online") throw gameNotStarted
        fun(gamePoints)
      } getOrElse (throw notPartOfGame)
    }

    def taskIsValid(fun: (String, Int, Int, Int, Boolean, Int) => UserTaskStatus)(implicit session: Session): UserTaskStatus = {
      val tq = for (
        t <- models.Tasks if t.gameId === gid && t.id === tid
      ) yield (t.ttype, t.maxpoints, t.maxattempts, t.minToAccept, t.active, t.penalty)

      tq.firstOption map { row =>
        if (!row._5) throw taskNotActive
        fun.tupled (row)
      } getOrElse (throw taskNotFound)
    }

    def userCanSendAnswer(maxattempts: Int)(fun: (Int, Int) => UserTaskStatus)(implicit session: Session): UserTaskStatus = {
      val utq = for (
        ut <- models.UserTasks if ut.userId === user.id && ut.gameId === gid && ut.taskId === tid
      ) yield (ut.attempts, ut.points)

      utq.firstOption map { case (attempts, prevPoints) =>
        if (attempts == maxattempts) throw noMoreAttempts
        fun(attempts, prevPoints) 
      } getOrElse fun(0, 0)
    }

    def updateUserTaskStatus(status: String, points: Int, attempt: Int)(implicit session: Session) {
      val someNow = Some(DateTime.now)
      if (attempt == 1)
        models.UserTasks insert UserTask(user.id, gid, tid, status, points, attempt, someNow)
      else {
        val utq = for (
          ut <- models.UserTasks if ut.userId === user.id && ut.gameId === gid && ut.taskId === tid
        ) yield (ut.status ~ ut.points ~ ut.attempts ~ ut.time)
        utq update (status, points, attempt, someNow)
      }
    }

    def getStatusAndPoints(ttype: String, maxpoints: Int, minToAccept: Int, penalty: Int)(implicit session: Session): (String, Int) = {
      if (isABCTask(ttype)) {
        val selected = ans.options getOrElse (throw expectedField("options"))
        val abcq = for (
          abc <- models.ABCTasks if abc.gameId === gid && abc.taskId === tid
        ) yield (abc.char, abc.points)

        val s = abcq.elements map { case (char, points) => if (selected contains char) points else 0 } sum
        val sp = s - penalty
        if (sp >= minToAccept)
          ("accepted", sp)
        else
          ("rejected", sp)
      }
      else if (isGPSTask(ttype)) {
        val userLat = ans.lat getOrElse (throw expectedField("lat"))
        val userLon = ans.lon getOrElse (throw expectedField("lon"))
        val gpsq = for (
          gps <- models.GPSTasks if gps.gameId === gid && gps.taskId === tid
          if geodistance(gps.lat, gps.lon, userLat, userLon) < gps.range
        ) yield gps.length

        val sp = maxpoints - penalty
        if (gpsq.first > 0)
          ("accepted", sp)
        else
          ("rejected", sp)
      }
      else
        throw unexpectedTaskType(ttype)
    }

    def updateUserGamePoints(points: Int, prev: Int, gamePoints: Int)(implicit session: Session) {
      val ugq = for (
        ug <- models.UserGames if ug.userId === user.id && ug.gameId === gid
      ) yield (ug.points)
      ugq update (gamePoints + points - prev)      
    }

    db withSession { implicit session =>
      userGameIsValid { gamePoints =>
        taskIsValid { case (ttype, maxpoints, maxattempts, minToAccept, active, penalty) =>
          userCanSendAnswer (maxattempts) { case (attempts, prevPoints) =>
            val attempt = attempts + 1
            val (status, points) = getStatusAndPoints(ttype, maxpoints, minToAccept, attempts*penalty)
            updateUserTaskStatus(status, points, attempt)
            updateUserGamePoints(points, prevPoints, gamePoints)
            return UserTaskStatus(status, attempt, points, attempt < maxattempts)
          }
        }
      }
    }
  }
  
  private def canShowTask(t: models.Tasks.type, user: User, lat: Double, lon: Double) =
    t.active === false || (for (g <- models.Games if g.id === t.gameId && (
      g.status === "ended" || 
      g.status === "online" && 
      (t.rangeLimit.isNull || geodistance(t.lat, t.lon, lat, lon) < t.rangeLimit) && 
      (t.timeLimit.isNull || t.timeLimit < DateTime.now)
    )) yield g).exists

  private def isABCTask(ttype: String) = 
    ttype contains "ABC"
    
  private def isGPSTask(ttype: String) = 
    ttype contains "GPS"

  private def updateNumberOfPlayers(gid: Int, a: Int)(implicit session: Session) {
    val q = for (g <- models.Games if g.id === gid) yield g.numberOfPlayers
    val num = q.first
    q update (num+a)
  }

  private val geodistanceFun = SimpleFunction[Int]("geodistance")
  private def geodistance(lat1: Column[Option[Double]], lon1: Column[Option[Double]], lat2: Double, lon2: Double) =
    geodistanceFun(Seq(lat1, lon1, lat2, lon2))
    
  private val PublicGamesView = models.Games filter (g => g.status === "published" || g.status === "online")
  private val GamesView = PublicGamesView innerJoin models.Operators on (_.operatorId === _.id)
  private def GamesViewWithDistances(lat: Double, lon: Double) = 
    GamesView map { case (g, o) =>
      (g, o, (for (t <- models.GPSTasks if t.gameId === g.id) yield geodistance(t.lat, t.lon, lat, lon)).min)
    }
  private def UserGamesView(uid: Int) =
    models.UserGames filter (_.userId === uid) innerJoin models.Games on (_.gameId === _.id)
  private def UserTasksView(uid: Int, gid: Int) =
    models.UserTasks filter (ut => ut.userId === uid && ut.gameId === gid) innerJoin models.Tasks on (_.gameId === _.gameId)

  private val sha256 = java.security.MessageDigest.getInstance("SHA-256")
  private def hash(login: String, password: String) = {
    sha256.digest((login+":"+password).getBytes("UTF-8")) map { "%02X" format _ } mkString
  }

  private val msgPre = "webapi.error."
  private val gameNotFound   = new ApiException(404, msgPre+"gameNotFound")
  private val taskNotFound   = new ApiException(404, msgPre+"taskNotFound")
  private val userExists     = new ApiException(409, msgPre+"userExists")
  private val notPartOfGame  = new ApiException(404, msgPre+"notPartOfGame")
  private val gameNotStarted = new ApiException(400, msgPre+"notStarted")
  private val gameIsEnded    = new ApiException(400, msgPre+"isEnded")
  private val alreadyInGame  = new ApiException(400, msgPre+"alreadyJoined")
  private val gameIsFull     = new ApiException(400, msgPre+"isFull")
  private val alreadyLeft    = new ApiException(400, msgPre+"alreadyLeft")
  private val taskNotActive  = new ApiException(400, msgPre+"taskNotActive")
  private val noMoreAttempts = new ApiException(400, msgPre+"noMoreAttempts")
  private def userMustWait(m: Long) = new ApiException(400, msgPre+"mustWait", Seq(m))
  private def expectedField(f: String) = new ApiException(400, msgPre+"expectedField", Seq(f))
  private def unexpectedTaskType(c: String) = new ApiException(500, msgPre+"unexpectedTaskType", Seq(c))
}

