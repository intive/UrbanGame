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

import play.api.Play.current
import play.api.db.slick.DB
import play.api.db.slick.Config.driver.simple._
import java.sql.Timestamp
import scala.language.postfixOps
import models.utils._
import com.github.tototoshi.slick.JodaSupport._
import com.github.nscala_time.time.Imports._

object Tasks extends Table[TasksDetails]("TASKS") {
  def id = column[Int]("id", O.NotNull)
  def gameId = column[Int]("gameId", O.NotNull)
  def version = column[Int]("version", O.NotNull, O.Default(1))
  def name = column[String]("name", O.NotNull)
  def description = column[String]("description", O.NotNull)
  def ttype = column[String]("type", O.NotNull)
  def maxpoints = column[Int]("maxpoints", O.NotNull)
  def minToAccept = column[Int]("minToAccept", O.NotNull, O.Default(1))
  def maxattempts = column[Int]("maxattempts", O.NotNull)
  def timeLimit = column[Option[DateTime]]("timeLimit")
  def lat   = column[Option[Double]]("lat")
  def lon   = column[Option[Double]]("lon")
  def rangeLimit = column[Option[Int]]("rangeLimit")
  def active = column[Boolean]("active", O.NotNull, O.Default(true))
  def penalty = column[Int]("penalty", O.NotNull, O.Default(0))
  def * = id.? ~ gameId ~ version ~ ttype ~ name ~ description ~ maxpoints ~ minToAccept ~ maxattempts ~ timeLimit ~ lat ~ lon ~ rangeLimit ~ active ~ penalty <> (TasksDetails, TasksDetails.unapply _)
  def pk = primaryKey("TASKS_PK", (gameId, id))
  def game = foreignKey("GMT_FK", gameId, Games)(_.id)
}

trait Tasks { this: ImplicitSession =>
  import scala.util.{ Try, Success, Failure }
  import models.dal._

  def getRowsNo: Int = (for {t <- Tasks} yield t.length).first

  def getGameTasksNo(gid: Int): Int = (for {t <- Tasks if t.gameId === gid} yield t.length).first

  def createTask(td: TasksDetails): Try[Int] = {
    val in = Try { 
      val tid = (for (t <- Tasks if t.gameId === td.gameId) yield t.length).first
      val td1 = TasksDetails( 
        Some(tid), td.gameId, td.version, td.ttype, td.name, td.description,
        td.maxpoints, td.minToAccept, td.maxattempts,
        td.timeLimit, td.lat, td.lon, td.rangeLimit,
        td.active, td.penalty)
      Tasks insert td1
      tid
    }

    in match {
      case Success(a) => Bridges.Games.updateTasksNo(td.gameId, getGameTasksNo(td.gameId))
      case Failure(e) => 
    }

    in
  }

  def findAll: Seq[TasksDetails] = Query(Tasks).list
}
