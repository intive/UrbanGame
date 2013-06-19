/**Copyright 2013 BLStream, BLStream's Patronage Program Contributors
 *     http://blstream.github.com/UrbanGame/
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package models

import scala.slick.session._
import scala.language.postfixOps
import models.utils._
import play.api.libs.json._
import play.api.libs.functional.syntax._
import scala.slick.session.Database
import play.api.db.slick.Config.driver.simple._
import play.api.Play.current
import com.github.nscala_time.time.Imports._
import scala.util.{ Try, Success, Failure }

object dal {

  object Bridges extends Bridges {

    implicit val gamesListWrites = Json.writes[GamesList]
    implicit val tasksListReads = Json.reads[TasksDetails]
    implicit val gamesDetailsReads = Json.reads[GamesDetails]
    implicit val gamesDetailsWrites = Json.writes[GamesDetails]
    implicit val gamesReads = Json.reads[GamePartData]

    def Games(implicit session: Session) = new ImplicitSession with Games { override val implicitSession = session }
    def Operators(implicit session: Session) = new ImplicitSession with Operators { override val implicitSession = session }
    def Tasks(implicit session: Session) = new ImplicitSession with Tasks { override val implicitSession = session }
    def Skins(implicit session: Session) = new ImplicitSession with Skins { override val implicitSession = session }

    val db = play.api.db.slick.DB

    def game(gid: Int): Try[GamesDetails] = db.withSession { implicit session =>
      Games.findGameById(gid)
    }

    def gameList(opId: Int): List[GamesList] = db.withSession { implicit session =>
      Games.findGamesListByOperatorId(opId)
    }

    def gameArchives(opId: Int): List[GamesList] = db.withSession { implicit session =>
      Games.findGamesArchiveByOperatorId(opId)
    }

    def gameSave(res: GamePartData, oid: Int): Try[Int] = {
      val gd = GamesDetails(id = None, name = res.name, description = res.description, location = res.location, operatorId = oid, 
        created = DateTime.now, startTime = mutils.combineDate(res.startDate, res.startTime), endTime = mutils.combineDate(res.endDate, res.endTime), 
        winning = res.winning, nWins = res.winningNum, difficulty = res.diff, maxPlayers = res.playersNum, awards = res.awards, status = res.status, 
        tasksNo = res.tasksNo) 
          
      db.withSession { implicit session =>
        Games.create(gd)
      }
    }

    def gameUpdate(res: GamePartData, gid: Int, oid: Int): Try[Int] = {
      val gd = GamesDetails(id = Some(gid), name = res.name, description = res.description, location = res.location, operatorId = oid, 
        created = DateTime.now, startTime = mutils.combineDate(res.startDate, res.startTime), endTime = mutils.combineDate(res.endDate, res.endTime), 
        winning = res.winning, nWins = res.winningNum, difficulty = res.diff, maxPlayers = res.playersNum, awards = res.awards, status = res.status, 
        tasksNo = res.tasksNo) 
          
      db.withSession { implicit session =>
        Games.update(gid, gd)
      }
    }

    def gameDelete(gid: Int): Try[Int] = db.withSession { implicit session =>
      Games.delete(gid)
    }

    def searchName(name: String): Try[Boolean] = {
      val cntName: Int = db.withSession { implicit session =>
        Games.checkName(name)
      }
      Try(cntName == 0)
    }

    def gameChangeStatus(id: Int, flag: String): Try[Int] = db.withSession { implicit session =>
      Games.changeStatus(id, flag)
    }

    def findOperatorId(gid: Int): Int = db.withSession { implicit session =>
      Games.findOperatorId(gid)
    }

    def findById(oid: Int): Option[Operator] =db.withSession { implicit session =>
      Operators.findById(oid)
    }

    def findByEmail(email: String): Option[Operator] = db.withSession { implicit session =>
      Operators.findByEmail(email)
    }

    def auth(email: String, password: String): Option[Operator] = db.withSession { implicit session =>
      Operators.authenticate(email, password)
    }

    def updateSignUpToken(email: String, token: Option[String]): Try[Int] = db.withSession { implicit session =>
      Operators.updateToken(email, token)
    }

    def operatorSave(op: Operator): Try[Int] = db.withSession { implicit session =>
      Operators.create(op)
    }

    def findOperatorByCookie(cookie: String) = db.withSession { implicit session =>
      Operators.findByCookie(cookie)
    }
  }
}

trait Bridges {
  case class GamePartData(name: String, version: Int, description: String, location: String, startTime: String, startDate: String, 
    endTime: String, endDate: String, winning: String, winningNum: Int, diff: String, playersNum: Int, awards: String, 
    status: String, tasksNo: Int)

  def game(gid: Int): Try[GamesDetails]
  def gameList(opId: Int): List[GamesList]
  def gameSave(res: GamePartData, oid: Int): Try[Int]
  def gameUpdate(res: GamePartData, gid: Int, oid: Int): Try[Int]
  def gameDelete(gid: Int): Try[Int]
  def searchName(name: String): Try[Boolean]
  def gameChangeStatus(id: Int, flag: String): Try[Int]
  def findOperatorId(gid: Int): Int
  def findById(oid: Int): Option[Operator]
  def findByEmail(email: String): Option[Operator]
  def updateSignUpToken(email: String, token: Option[String]): Try[Int]
  def findOperatorByCookie(cookie: String)
}


trait ImplicitSession {
  implicit val implicitSession: Session
}