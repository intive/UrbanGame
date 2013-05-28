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
import com.github.tototoshi.slick.JodaSupport._
import com.github.nscala_time.time.Imports._
import models.mutils._

object Skins extends Table[SkinsDetails]("SKINS") {
  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def gameId = column[Int]("gameId", O.NotNull)
  def icon = column[String]("icon", O.NotNull, O.Default("games/gameicon.png"))
  def * = id.? ~ gameId ~ icon <> (SkinsDetails, SkinsDetails.unapply _)

  def forInsert = gameId ~ icon <> ({ t => 
      SkinsDetails(None, t._1, t._2)}, 
      { (sd: SkinsDetails) => Some((sd.gameId, sd.icon)) })

  def game = foreignKey("GM_FK", gameId, Games)(_.id)

}

trait Skins { this: ImplicitSession =>

  def getRowsNo: Int = (for {s <- Skins} yield s.length).first

  def getGameSkins(gid: Int): SkinsDetails = {
    val q = for {
      s <- Skins if s.gameId === gid.bind
    } yield s
    q.first
  }
}
