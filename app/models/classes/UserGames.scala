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
import models.utils._

object UserGames extends Table[UserGame]("USERGAMES") {
  def userId = column[Int]("userId", O.NotNull)
  def gameId = column[Int]("gameId", O.NotNull)
  def joined = column[DateTime]("joined", O.NotNull, O.Default(DateTime.now))
  def left   = column[Option[DateTime]]("left")
  def points = column[Int]("points", O.NotNull, O.Default(0))
  def * = userId ~ gameId ~ joined ~ left ~ points <> (UserGame, UserGame.unapply _)
  def pk = primaryKey("USERSGAMES_PK", (userId, gameId))

  def user = foreignKey("USERGAMES_USERS_FK", userId, Users)(_.id)
  def game = foreignKey("USERGAMES_GAMES_FK", gameId, Games)(_.id)
}

