
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

import play.api.db.slick.Config.driver.simple._
import com.github.tototoshi.slick.JodaSupport._
import com.github.nscala_time.time.Imports._

case class UserTask(userId: Int, gameId: Int, taskId: Int, status: String, points: Int, attempts: Int, time: Option[DateTime])

object UserTasks extends Table[UserTask]("USERTASKS") {
  def userId = column[Int]("userId", O.NotNull)
  def gameId = column[Int]("gameId", O.NotNull)
  def taskId = column[Int]("taskId", O.NotNull)
  def status = column[String]("status", O.NotNull, O.Default("not sent"))
  def points = column[Int]("points", O.NotNull, O.Default(0))
  def attempts = column[Int]("attempts", O.NotNull, O.Default(0))
  def time = column[Option[DateTime]]("time")
  def * = userId ~ gameId ~ taskId ~ status ~ points ~ attempts ~ time <> (UserTask, UserTask.unapply _)
  def pk = primaryKey("USERTASKS_PK", (userId, gameId, taskId))
  def user = foreignKey("USERTASKS_USERS_FK", userId, Users)(_.id)
  def game = foreignKey("USERTASKS_GAMES_FK", gameId, Games)(_.id)
  def task = foreignKey("USERTASKS_TASKS_FK", (gameId, taskId), Tasks)(t => (t.gameId, t.id))
}

