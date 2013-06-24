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

object Notifications extends Table[Notification]("NOTIFICATIONS") {
  def id = column[Int]("id", O.NotNull, O.AutoInc)
  def gameId = column[Int]("gameId", O.NotNull)
  def version = column[Int]("version", O.NotNull, O.Default(1))
  def notification = column[String]("name", O.NotNull)
  def date = column[DateTime]("deadline", O.NotNull)
  def * = id.? ~ gameId ~ version ~ notification ~ date <> (Notification, Notification.unapply _)
  def forInsert = gameId ~ version ~ notification ~ date <> ({ t => 
      Notification(None, t._1, t._2, t._3, t._4)}, 
      { (nt: Notification) => Some((nt.gameId, nt.version, nt.notification, nt.date))
      })

  def game = foreignKey("GMN_FK", gameId, Games)(_.id)
}

trait Notifications { this: ImplicitSession =>
  import scala.util.{ Try, Success, Failure }
  import models.dal._

  def getRowsNo: Int = (for {n <- Notifications} yield n.length).first

  def create(nt: Notification): Try[Int] = Try(Notifications.forInsert returning Notifications.id insert nt)

  def findAll: Seq[Notification] = Query(Notifications).list
}