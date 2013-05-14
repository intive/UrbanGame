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
package controllers

import play.api._
import play.api.mvc._
import play.api.db.slick.Config.driver.simple._
import play.api.db.DB
import scala.slick.session.Database
import play.api.Play.current
import com.github.nscala_time.time.Imports._
import models.mutils._
import models.dal.Bridges._
import play.api.Logger

object GamesCtrl extends Controller with CookieLang {

  def newgame = Action { implicit request =>
    Ok(Scalate("newgame").render('title -> "Urban Game - Create new game", 'request -> request))
  }

  def mygames = Action { implicit request =>
    Ok(Scalate("mygames").render('title -> "Urban Game - Create new game", 'request -> request))
  }

  import play.api.libs.json._
  import play.api.libs.functional.syntax._
  import scala.language.existentials

  def getGamesList = Action { implicit request =>
    val opId = 2 // will be set from session soon

    val glist = play.api.db.slick.DB.withSession { implicit session =>
        Games.getOperatorGamesList(opId)
    }

    Ok(Json.toJson(glist))
  }

  def saveGame = Action { implicit request =>
    val gd = GamesDetails(None, "gg1", 1, "Game1", "Wro", 1, DateTime.now, DateTime.now, 
        DateTime.now + 600.hours, DateTime.now, DateTime.now + 600.hours, "max_points", 1, 
        "easy", 20, "Jakieś", "online")

    val gid = play.api.db.slick.DB.withSession { implicit session =>
      Games.createGame(gd)
    }

    Ok(Json.toJson(gid))
  }

  def updateGame(gid: Int) = Action { implicit request =>
    Ok(Json.toJson(gid))
  }

  def deleteGame(gid: Int) = Action { implicit request =>
    Ok(Json.toJson(gid))
  }

}