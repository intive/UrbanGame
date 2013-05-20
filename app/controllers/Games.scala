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
import play.api.Logger

object GamesCtrl extends Controller with CookieLang {

  def newGame = Action { implicit request =>
    Ok(Scalate("newgame").render('title -> "Urban Game - Edit the game", 'request -> request))
  }

  def editGame(gid: Int) = Action { implicit request =>
    Ok(Scalate("newgame").render('title -> "Urban Game - Create new game", 'request -> request))
  }

  def myGames = Action { implicit request =>
    Ok(Scalate("mygames").render('title -> "Urban Game - My games", 'request -> request))
  }

  def gameInfo(gid: Int) = Action { implicit request =>
    Ok(Scalate("gameinfo").render('title -> "Urban Game - Game informations", 'request -> request))
  }

  def gamePlayers(gid: Int) = Action { implicit request =>
    Ok(Scalate("gameinfo").render('title -> "Urban Game - Game informations", 'request -> request))
  }

  def gameTasks(gid: Int) = Action { implicit request =>
    Ok(Scalate("gameinfo").render('title -> "Urban Game - Game informations", 'request -> request))
  }

  def gameSkin(gid: Int) = Action { implicit request =>
    Ok(Scalate("gameinfo").render('title -> "Urban Game - Game informations", 'request -> request))
  }

  def gameMessages(gid: Int) = Action { implicit request =>
    Ok(Scalate("gameinfo").render('title -> "Urban Game - Game informations", 'request -> request))
  }

  def gameArchive = Action { implicit request =>
    Ok("Archive")
  }

  def options = Action { implicit request =>
    Ok("Options")
  }

  import play.api.libs.json._
  import play.api.libs.functional.syntax._
  import play.api.db.slick.Config.driver.simple._
  import play.api.db.DB
  import scala.slick.session.Database
  import play.api.Play.current
  import com.github.nscala_time.time.Imports._
  import models.mutils._
  import models.dal.Bridges._
  import scala.language.existentials

  def getGamesList = Action { implicit request =>
    val opId = 1 // will be set from session soon

    val glist = play.api.db.slick.DB.withSession { implicit session =>
      Games.getOperatorGamesList(opId)
    }

    Ok(Json.toJson(glist))
  }

  def saveGame = Action { implicit request =>
    request.body.asJson.map { json =>
      ( json \ "game").validate[SmallGame].fold (
        valid = { res => 
          val gd = GamesDetails(id = None, name = res.name, description = res.description, location = res.location, operatorId = 1, 
            startTime = combineDate(res.startDate, res.startTime), endTime = combineDate(res.endDate, res.endTime), 
            winning = res.winning, nWins = res.winningNum, difficulty = res.diff, maxPlayers = res.playersNum, awards = res.awards) 
          
          val gid: Int = play.api.db.slick.DB.withSession { implicit session =>
            Games.createGame(gd)
          }

          Ok(Json.toJson(Map("id" -> gid)))
        },
        invalid = ( e => BadRequest(e.toString) )
      )
    }.getOrElse{
      BadRequest("Detected error, JSON expected.")
    }
  }

  def updateGame(gid: Int) = Action { implicit request =>
    request.body.asJson.map { json =>
      ( json \ "game").validate[SmallGame].fold (
        valid = { res => 
          val gd = GamesDetails(id = Some(gid), name = res.name, description = res.description, location = res.location, operatorId = 1, 
            startTime = combineDate(res.startDate, res.startTime), endTime = combineDate(res.endDate, res.endTime), 
            winning = res.winning, nWins = res.winningNum, difficulty = res.diff, maxPlayers = res.playersNum, awards = res.awards) 
          
          val ugid: Int = play.api.db.slick.DB.withSession { implicit session =>
            Games.updateGame(gid, gd)
          }

          Ok(Json.toJson(Map("id" -> ugid)))
        },
        invalid = ( e => BadRequest(e.toString) )
      )
    }.getOrElse{
      BadRequest("Detected error, JSON expected.")
    }
    
  }

  def deleteGame(gid: Int) = Action { implicit request =>
    val dcnt: Int = play.api.db.slick.DB.withSession { implicit session =>
      Games.deleteGame(gid)
    }

    if(dcnt > 0)
      Ok(Json.toJson(Map("msg" -> "The game has been deleted")))
    else
      BadRequest("Detected error, the game could not be deleted")
  }

  def checkName = Action { implicit request =>
    request.body.asJson.map { json =>
      ( json \ "name").validate[String].fold (
        valid = { name => 
          val cntName: Int = play.api.db.slick.DB.withSession { implicit session =>
            Games.checkName(name)
          }
          
          val isValidName: Boolean = cntName match {
            case 0 => true
            case _ => false
          }
          Ok(Json.toJson(Map("valid" -> isValidName)))
        },
        invalid = ( e => BadRequest(e.toString) )
      )
    }.getOrElse{
      BadRequest("Detected error, JSON expected.")
    }
  }

}