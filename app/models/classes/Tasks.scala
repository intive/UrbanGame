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
  def id = column[Int]("id", O.NotNull, O.AutoInc)
  def gameId = column[Int]("gameId", O.NotNull)
  def version = column[Int]("version", O.NotNull, O.Default(1))
  def name = column[String]("name", O.NotNull)
  def description = column[String]("description", O.NotNull)
  def category = column[String]("category", O.NotNull)
  def maxpoints = column[Int]("maxpoints", O.NotNull)
  def maxattempts = column[Int]("maxattempts", O.NotNull)
  def timeLimit = column[Option[DateTime]]("timeLimit")
  def lat   = column[Option[Double]]("lat")
  def lon   = column[Option[Double]]("lon")
  def rangeLimit = column[Option[Double]]("rangeLimit")
  def cancelled = column[Boolean]("cancelled", O.NotNull, O.Default(false))
  def penalty = column[Int]("penalty", O.NotNull)
  def * = id.? ~ gameId ~ version ~ category ~ name ~ description ~ maxpoints ~ maxattempts ~ timeLimit ~ lat ~ lon ~ rangeLimit ~ cancelled ~ penalty <> (TasksDetails, TasksDetails.unapply _)
  def forInsert = gameId ~ version ~ category ~ name ~ description ~ maxpoints ~ maxattempts ~ timeLimit ~ lat ~ lon ~ rangeLimit ~ cancelled ~ penalty <> ({ t => 
    TasksDetails(None, t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9, t._10, t._11, t._12, t._13)},
      { (td: TasksDetails) => Some((td.gameId, td.version, td.category, td.name, td.description, td.maxpoints, td.maxattempts, td.timeLimit, td.lat, td.lon, td.rangeLimit, td.cancelled, td.penalty))
      })
  def game = foreignKey("GMT_FK", gameId, Games)(_.id)
}

trait Tasks { this: ImplicitSession =>
  import scala.util.{ Try, Success, Failure }
  import models.dal._

  def getRowsNo: Int = (for {t <- Tasks} yield t.length).first

  def getGameTasksNo(gid: Int): Int = (for {t <- Tasks if t.gameId === gid} yield t.length).first

  def createTask(td: TasksDetails): Try[Int] = {
    val in = Try(Tasks.forInsert returning Tasks.id insert td)

    in match {
      case Success(a) => Bridges.Games.updateTasksNo(td.gameId, getGameTasksNo(td.gameId))
      case Failure(e) => 
    }

    in
  }

  def findAll: Seq[TasksDetails] = Query(Tasks).list
}
