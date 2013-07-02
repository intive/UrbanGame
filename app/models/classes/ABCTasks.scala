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

import play.api.db.slick.DB
import play.api.db.slick.Config.driver.simple._
import scala.language.postfixOps
import models.utils._

case class ABCTaskOption(gid: Int, tid: Int, char: String, option: String, points: Int)

object ABCTasks extends Table[ABCTaskOption]("ABCTASKS") {
  def taskId = column[Int]("taskId", O.NotNull)
  def gameId = column[Int]("gameId", O.NotNull)
  def char   = column[String]("character", O.NotNull)
  def option = column[String]("option", O.NotNull)
  def points = column[Int]("points", O.NotNull)
  def * = gameId ~ taskId ~ char ~ option ~ points <> (ABCTaskOption, ABCTaskOption.unapply _)
  def pk = primaryKey("ABCTASKS_PK", (gameId, taskId, char))
  def game = foreignKey("ABCTASKS_GAMES_FK", gameId, Games)(_.id)
  def task = foreignKey("ABCTASKS_TASKS_FK", (gameId, taskId), Tasks)(t => (t.gameId, t.id))
}

