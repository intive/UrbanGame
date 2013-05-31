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
import play.api.libs.json._
import play.api.libs.functional.syntax._
import scala.util.{ Try, Success, Failure }

object GamesCtrl extends Controller with CookieLang with GamesCtrl {

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
    Ok(Scalate("gameinfo").render('title -> "Urban Game - Players list", 'request -> request))
  }

  def gameTasks(gid: Int) = Action { implicit request =>
    Ok(Scalate("gameinfo").render('title -> "Urban Game - Tasks list", 'request -> request))
  }

  def gameSkin(gid: Int) = Action { implicit request =>
    Ok(Scalate("gameinfo").render('title -> "Urban Game - Game skin", 'request -> request))
  }

  def gameMessages(gid: Int) = Action { implicit request =>
    Ok(Scalate("gameinfo").render('title -> "Urban Game - Game messages", 'request -> request))
  }

  def gameArchive = Action { implicit request =>
    Ok("Archive")
  }

  def options = Action { implicit request =>
    Ok("Options")
  }

  import scala.language.existentials
  import play.api.libs.json._
  import play.api.libs.functional.syntax._
  import models.dal.Bridges._

  def getGamesList = Action { implicit request =>
    val opId = 1 // will be set from session soon

    val glist = gameList(opId)

    Ok(Json.toJson(glist))
  }

  def getGame(gid: Int) = Action { implicit request =>
    game(gid) match {
      case Success(a) => Ok(Json.toJson(a))
      case Failure(e) => JsonBad(e.toString)
    }
  }

  def saveGame = Action { implicit request =>
    JsonData { res: GamePartData =>
      matchResult(gameSave(res))
    }
  }

  def updateGame(gid: Int) = Action { implicit request =>
    JsonData { res: GamePartData =>
      matchResult(gameUpdate(res, gid))
    }
  }

  def deleteGame(gid: Int) = Action { implicit request =>
    matchResult(gameDelete(gid))
  }

  def checkName = Action { implicit request =>
    JsonData { res: String =>
      matchResult(searchName(res), "Boolean")
    }
  }

  def cancelGame = Action { implicit request =>
    JsonData { res: Int =>
      matchResult(gameChangeStatus(res, "cancel"))
    }
  }

  def publishGame = Action { implicit request =>
    JsonData { res: Int =>
      matchResult(gameChangeStatus(res, "publish"))
    }
  }

  private def JsonData[A] (f: A => Result) (implicit request: Request[AnyContent], r: Reads[A]) = 
    request.body.asJson.map { json =>
      ( json \ "data").validate (r) map (f) recoverTotal {e => JsonBad(e.toString)}
    } getOrElse {
      JsonBad("JSON expected")
    }

  private def matchResult(res: Try[Any], ctype: String = "Int") = res match {
    case Success(a) => JsonOk(a, ctype)
    case Failure(e) => JsonBad(e.toString)
  }

  private def JsonOk(x: Any, ctype: String) = {
    ctype match {
      case "String" => Ok(Json.toJson(Map("val" -> x.asInstanceOf[String])))
      case "Int" => Ok(Json.toJson(Map("val" -> x.asInstanceOf[Int])))
      case "Boolean" => Ok(Json.toJson(Map("val" -> x.asInstanceOf[Boolean])))
      case _ => JsonBad("No such type value, can't cast to given type")
    }
    
  }

  private def JsonBad(x: String) = {
    BadRequest(Json.toJson(Map("error" -> x.asInstanceOf[String])))
  }
}

trait GamesCtrl {

}