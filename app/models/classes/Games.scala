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
import slick.lifted.{MappedTypeMapper, BaseTypeMapper}
import java.sql.Timestamp
import scala.language.postfixOps
import models.mutils._
import com.github.tototoshi.slick.JodaSupport._
import com.github.nscala_time.time.Imports._


object Games extends Table[GamesDetails]("GAMES") {
  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def name = column[String]("name", O.NotNull)
  def version = column[Int]("version", O.NotNull, O.Default(1))
  def description = column[String]("description", O.NotNull)
  def location = column[String]("location", O.NotNull)
  def lat = column[Float]("lat", O.NotNull)
  def lon = column[Float]("lon", O.NotNull)
  def operatorId = column[Int]("operatorId", O.NotNull)
  def created = column[DateTime]("created", O.NotNull, O.Default(DateTime.now))
  def startTime = column[DateTime]("startTime", O.NotNull, O.Default(DateTime.now))
  def endTime = column[DateTime]("endTime", O.NotNull)
  def started = column[Option[DateTime]]("started")
  def ended = column[Option[DateTime]]("ended")
  def winning = column[String]("winning", O.NotNull, O.Default("max_points"))
  def nWins = column[Int]("nWins", O.NotNull, O.Default(1))
  def difficulty = column[String]("difficulty", O.NotNull, O.Default("easy"))
  def maxPlayers = column[Int]("maxPlayers", O.NotNull, O.Default(1000000))
  def awards = column[String]("awards", O.NotNull)
  def status = column[String]("status", O.NotNull, O.Default("project"))
  def image = column[String]("image", O.NotNull, O.Default("games/gameicon.png"))
  def * = id.? ~ name ~ version ~ description ~ location ~ lat ~ lon ~ operatorId ~ 
    created ~ startTime ~ endTime ~ started ~ ended ~ winning ~ nWins ~ 
    difficulty ~ maxPlayers ~ awards ~ status ~ image <> (GamesDetails, GamesDetails.unapply _)

  def forInsert = name ~ version ~ description ~ location ~ lat ~ lon ~ operatorId ~ 
    created ~ startTime ~ endTime ~ started ~ ended ~ winning ~ nWins ~ 
    difficulty ~ maxPlayers ~ awards ~ status ~ image <> ({ t => 
      GamesDetails(None, t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9, 
        t._10, t._11, t._12, t._13, t._14, t._15, t._16, t._17, t._18, t._19)}, 
      { (gd: GamesDetails) => Some((gd.name, gd.version, gd.description, gd.location, gd.lat, 
        gd.lon, gd.operatorId, gd.created, gd.startTime, gd.endTime, gd.started, gd.ended, 
        gd.winning, gd.nWins, gd.difficulty, gd.maxPlayers, gd.awards, gd.status, gd.image))
      })

  def operator = foreignKey("OP_FK", operatorId, Operators)(_.id)

}

trait Games { this: ImplicitSession =>

  def getRowsNo: Int = (for {g <- Games} yield g.length).first

  def checkGamesStatus = {
    val q = for {
      g <- Games 
      if g.status === "online" && g.endTime <= DateTime.now
    } yield g.status
    val p = for {
      g <- Games 
      if g.status === "published" && g.startTime <= DateTime.now
    } yield g.status
    q update "finished"
    p update "online"
  }

  def getOperatorGamesList(id: Int): List[GamesList] = {
    checkGamesStatus
    val q = for {
      g <- Games 
      if g.operatorId === id.bind 
      if g.status =!= "finished"
    } yield (g.id, g.name, g.version, g.location, g.startTime, g.endTime, g.status, g.image)
    q.list map {
      case (id, name, ver, loc, st, et, stat, img) => GamesList(Some(id), name, ver, loc, st, et, stat, img)
    }
  }

  def getOperatorGamesArchive(id: Int): List[GamesList] = {
    val q = for {
      g <- Games 
      if g.operatorId === id.bind 
      if g.status === "finished"
    } yield (g.id, g.name, g.version, g.location, g.startTime, g.endTime, g.status, g.image)
    q.list map {
      case (id, name, ver, loc, st, et, stat, img) => GamesList(Some(id), name, ver, loc, st, et, stat, img)
    }
  }

  def createGame(gd: GamesDetails): Int = Games.forInsert returning Games.id insert gd

}


