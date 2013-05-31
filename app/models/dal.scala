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
    implicit val operatorsDataReads = Json.reads[OperatorsData]
    implicit val operatorsDataWrites = Json.writes[OperatorsData]

    def Games(implicit session: Session) = new ImplicitSession with Games { override val implicitSession = session }
    def Operators(implicit session: Session) = new ImplicitSession with Operators { override val implicitSession = session }
    def Tasks(implicit session: Session) = new ImplicitSession with Tasks { override val implicitSession = session }
    def Skins(implicit session: Session) = new ImplicitSession with Skins { override val implicitSession = session }

    def game(gid: Int): Try[GamesDetails] = play.api.db.slick.DB.withSession { implicit session =>
      Games.getGame(gid)
    }

    def gameList(opId: Int): List[GamesList] = play.api.db.slick.DB.withSession { implicit session =>
      Games.getOperatorGamesList(opId)
    }

    def gameSave(res: GamePartData): Try[Int] = {
      val gd = GamesDetails(id = None, name = res.name, description = res.description, location = res.location, operatorId = 1, 
        created = DateTime.now, startTime = mutils.combineDate(res.startDate, res.startTime), endTime = mutils.combineDate(res.endDate, res.endTime), 
        winning = res.winning, nWins = res.winningNum, difficulty = res.diff, maxPlayers = res.playersNum, awards = res.awards) 
          
      play.api.db.slick.DB.withSession { implicit session =>
        Games.createGame(gd)
      }
    }

    def gameUpdate(res: GamePartData, gid: Int): Try[Int] = {
      val gd = GamesDetails(id = Some(gid), name = res.name, description = res.description, location = res.location, operatorId = 1, 
        created = DateTime.now, startTime = mutils.combineDate(res.startDate, res.startTime), endTime = mutils.combineDate(res.endDate, res.endTime), 
        winning = res.winning, nWins = res.winningNum, difficulty = res.diff, maxPlayers = res.playersNum, awards = res.awards) 
          
      play.api.db.slick.DB.withSession { implicit session =>
        Games.updateGame(gid, gd)
      }
    }

    def gameDelete(gid: Int): Try[Int] = play.api.db.slick.DB.withSession { implicit session =>
      Games.deleteGame(gid)
    }

    def searchName(name: String): Try[Boolean] = {
      val cntName: Int = play.api.db.slick.DB.withSession { implicit session =>
        Games.checkName(name)
      }
      Try(cntName == 0)
    }

    def gameChangeStatus(id: Int, flag: String): Try[Int] = play.api.db.slick.DB.withSession { implicit session =>
      Games.changeStatus(id, flag)
    }
  }
}

trait Bridges {
  case class GamePartData(name: String, description: String, location: String, startTime: String, startDate: String, 
    endTime: String, endDate: String, winning: String, winningNum: Int, diff: String, playersNum: Int, awards: String)
  def game(gid: Int): Try[GamesDetails]
  def gameList(opId: Int): List[GamesList]
  def gameSave(res: GamePartData): Try[Int]
  def gameUpdate(res: GamePartData, gid: Int): Try[Int]
  def gameDelete(gid: Int): Try[Int]
  def searchName(name: String): Try[Boolean]
  def gameChangeStatus(id: Int, flag: String): Try[Int]
}


trait ImplicitSession {
  implicit val implicitSession: Session
}