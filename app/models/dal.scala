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
    implicit val operatorPartReads = Json.reads[OperatorPartData]
    implicit object permissionFormat extends Format[Permission] {
      def reads(json: JsValue): JsResult[Permission] = JsSuccess(Permission.valueOf((json \ "permission").as[String]))
      def writes(per: Permission): JsValue = Json.obj("permission" -> JsString(per.toString))
    }
    implicit val operatorWrites = Json.writes[Operator]

    def Games(implicit session: Session) = new ImplicitSession with Games { override val implicitSession = session }
    def Operators(implicit session: Session) = new ImplicitSession with Operators { override val implicitSession = session }
    def Tasks(implicit session: Session) = new ImplicitSession with Tasks { override val implicitSession = session }
    def Skins(implicit session: Session) = new ImplicitSession with Skins { override val implicitSession = session }
    def Notifications(implicit session: Session) = new ImplicitSession with Notifications { override val implicitSession = session }
    def Tokens(implicit session: Session) = new ImplicitSession with Tokens { override val implicitSession = session }

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
      val cntName: Option[Int] = db.withSession { implicit session =>
        Games.checkName(name)
      }
      Try(cntName == Some(0))
    }

    def gameChangeStatus(id: Int, flag: String): Try[Int] = db.withSession { implicit session =>
      Games.changeStatus(id, flag)
    }

    def findOperatorId(gid: Int): Int = db.withSession { implicit session =>
      Games.findOperatorId(gid)
    }

    def operatorSave(op: Operator): Try[Int] = db.withSession { implicit session =>
      Operators.create(op)
    }

    def operatorUpdatePass(op: Operator): Try[Int] = {
      db.withSession { implicit session =>
        Operators.update(op)
      }
    }

    def operatorUpdate(opd: OperatorPartData, oid: Int): Try[Int] = {
      import org.mindrot.jbcrypt.BCrypt
      val op = findOperatorById(oid)
      if(op != None) {
        var newPath = opd.logo

        if(opd.logo.contains("tmp")) {
          val path = play.api.Play.application.path + "/public/"
          val filename = opd.logo.substring(opd.logo.lastIndexOf("tmp/")+4, opd.logo.length)
          newPath = opd.logo.substring(0,opd.logo.lastIndexOf("tmp/")) + opd.logo.substring(opd.logo.lastIndexOf("tmp/")+4, opd.logo.length)
          new java.io.File(path + "upload/users/" + op.get.id.get +"/logo/" + filename).delete()
          val f = new java.io.File(path + opd.logo).renameTo(new java.io.File(path + newPath))
          new java.io.File(path + "upload/users/" + op.get.id.get +"/logo/tmp").delete()

        }

        val pass = if(opd.password != None) BCrypt.hashpw(opd.password.get, BCrypt.gensalt()) else op.get.password
        val o = op.get.copy(name = opd.name, description = Some(opd.description.getOrElse(op.get.description.getOrElse(""))), 
          password = pass, logo = newPath, modified = DateTime.now)
      
        db.withSession { implicit session =>
          Operators.update(o)
        }
      } else { Try(0) }

    }

    def findOperatorById(oid: Int): Option[Operator] = db.withSession { implicit session =>
      Operators.findById(oid)
    }

    def findOperatorByEmail(email: String): Option[Operator] = db.withSession { implicit session =>
      Operators.findByEmail(email)
    }

    def auth(email: String, password: String): Option[Operator] = db.withSession { implicit session =>
      Operators.authenticate(email, password)
    }

    def updateSignUpToken(email: String, token: Option[String]): Try[Int] = db.withSession { implicit session =>
      Operators.updateToken(email, token)
    }

    def findTokenByKeys(opId: Int, token: String, series: String): Option[Token] = db.withSession { implicit session =>
      Tokens.findByKeys(opId, token, series)
    }

    def saveToken(token: Token): Try[Int] = db.withSession { implicit session =>
      Tokens.create(token)
    }

    def deleteToken(id: Int, rm: Boolean): Try[Int] = db.withSession { implicit session =>
      Tokens.delete(id, rm)
    }

    def deleteToken(opId: Int, token: String, series: String, rm: Boolean): Try[Int] = db.withSession { implicit session =>
      Tokens.delete(Tokens.findByKeys(opId, token, series).get.id.get, rm)
    }
  }
}

trait Bridges {
  case class GamePartData(name: String, version: Int, description: String, location: String, startTime: String, startDate: String, 
    endTime: String, endDate: String, winning: String, winningNum: Int, diff: String, playersNum: Int, awards: String, 
    status: String, tasksNo: Int)
  case class OperatorPartData(name: String, description: Option[String], password: Option[String], logo: String)

  def game(gid: Int): Try[GamesDetails]
  def gameList(opId: Int): List[GamesList]
  def gameArchives(opId: Int): List[GamesList]
  def gameSave(res: GamePartData, oid: Int): Try[Int]
  def gameUpdate(res: GamePartData, gid: Int, oid: Int): Try[Int]
  def gameDelete(gid: Int): Try[Int]
  def searchName(name: String): Try[Boolean]
  def gameChangeStatus(id: Int, flag: String): Try[Int]
  def findOperatorId(gid: Int): Int
  def operatorSave(op: Operator): Try[Int]
  def operatorUpdatePass(op: Operator): Try[Int]
  def operatorUpdate(opd: OperatorPartData, oid: Int): Try[Int]
  def findOperatorById(oid: Int): Option[Operator]
  def findOperatorByEmail(email: String): Option[Operator]
  def auth(email: String, password: String): Option[Operator]
  def updateSignUpToken(email: String, token: Option[String]): Try[Int]
  def findTokenByKeys(opId: Int, token: String, series: String): Option[Token]
  def saveToken(token: Token): Try[Int]
  def deleteToken(id: Int, rm: Boolean): Try[Int]
  def deleteToken(opId: Int, token: String, series: String, rm: Boolean): Try[Int]
}


trait ImplicitSession {
  implicit val implicitSession: Session
}