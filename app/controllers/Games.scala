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
import play.api.Play.current
import jp.t2v.lab.play2.auth._
import play.api.cache.Cache
import models.utils._
import models.dal.Bridges._

object GamesCtrl extends Controller with CookieLang with AuthElement with AuthConfigImpl {

  def newGame = StackAction(AuthorityKey -> NormalUser) { implicit request =>
    val user = loggedIn
    Ok(Scalate("newgame").render('title -> "Urban Game - Edit the game", 'user -> Some(user)))
  }

  def myGames = StackAction(AuthorityKey -> NormalUser) { implicit request =>
    val user = loggedIn
    Ok(Scalate("mygames").render('title -> "Urban Game - My games", 'user -> Some(user)))
  }

  def gameArchive = StackAction(AuthorityKey -> NormalUser) { implicit request =>
    val user = loggedIn
    Ok(Scalate("mygames").render('title -> "Urban Game - My games", 'user -> Some(user)))
  }

  def options = StackAction(AuthorityKey -> NormalUser) { implicit request =>
    val user = loggedIn
    Ok(Scalate("options").render('title -> "Urban Game - Options", 'user -> Some(user)))
  }

  def editGame(gid: Int) = authAndValidAction(NormalUser, gid) { user => implicit request =>
    Ok(Scalate("newgame").render('title -> "Urban Game - Create new game", 'user -> Some(user)))
  }

  def gameInfo(gid: Int) = authAndValidAction(NormalUser, gid) { user => implicit request =>
    Ok(Scalate("gameinfo").render('title -> "Urban Game - Game informations", 'user -> Some(user)))
  }

  def gamePlayers(gid: Int) = authAndValidAction(NormalUser, gid) { user => implicit request =>
    Ok(Scalate("gameinfo").render('title -> "Urban Game - Players list", 'user -> Some(user)))
  }

  def gameTasks(gid: Int) = authAndValidAction(NormalUser, gid) { user => implicit request =>
    Ok(Scalate("gameinfo").render('title -> "Urban Game - Tasks list", 'user -> Some(user)))
  }

  def gameSkin(gid: Int) = authAndValidAction(NormalUser, gid) { user => implicit request =>
    Ok(Scalate("gameinfo").render('title -> "Urban Game - Game skin", 'user -> Some(user)))
  }

  def gameMessages(gid: Int) = authAndValidAction(NormalUser, gid) { user => implicit request =>
    Ok(Scalate("gameinfo").render('title -> "Urban Game - Game messages", 'user -> Some(user)))
  }

  import scala.language.existentials
  import play.api.libs.json._
  import play.api.libs.functional.syntax._

  def getGamesList = StackAction(AuthorityKey -> NormalUser) { implicit request =>
    val opId = loggedIn.id.get
    val glist = gameList(opId)

    Ok(Json.toJson(glist))
  }

  def getGamesArchive = StackAction(AuthorityKey -> NormalUser) { implicit request =>
    val opId = loggedIn.id.get
    val garchive = gameArchives(opId)

    Ok(Json.toJson(garchive))
  }

  def getGame(gid: Int) = authAndValidAction(NormalUser, gid) { user => implicit request =>
    game(gid) match {
      case Success(a) => Ok(Json.toJson(a))
      case Failure(e) => JsonBad(e.toString)
    }
  }

  def saveGame = StackAction(AuthorityKey -> NormalUser) { implicit request =>
    JsonData { res: GamePartData =>
      val oid = loggedIn.id.get
      matchResult(gameSave(res, oid))
    }
  }

  def updateGame(gid: Int) = StackAction(AuthorityKey -> NormalUser) { implicit request =>
    JsonData { res: GamePartData =>
      val oid = loggedIn.id.get
      matchResult(gameUpdate(res, gid, oid))
    }
  }

  def deleteGame(gid: Int) = authAndValidAction(NormalUser, gid) { user => implicit request =>
    matchResult(gameDelete(gid))
  }

  def checkName = StackAction(AuthorityKey -> NormalUser) { implicit request =>
    JsonData { res: String =>
      matchResult(searchName(res), "Boolean")
    }
  }

  def cancelGame = StackAction(AuthorityKey -> NormalUser) { implicit request =>
    JsonData { res: Int =>
      if(checkOperator(res)(loggedIn))
        matchResult(gameChangeStatus(res, "cancel"))
      else
        JsonBad("Access denied")
    }
  }

  def publishGame = StackAction(AuthorityKey -> NormalUser) { implicit request =>
    JsonData { res: Int =>
      if(checkOperator(res)(loggedIn))
        matchResult(gameChangeStatus(res, "publish"))
      else
        JsonBad("Access denied")
    }
  }

  def getProfile = StackAction(AuthorityKey -> NormalUser) { implicit request =>
    val user = loggedIn
    findOperatorById(user.id.get) match {
      case Some(x) => Ok(Json.toJson(x))
      case None => BadRequest(Json.toJson(Map("error" -> "No user found")))
    }
    
  }

  def updateProfile = StackAction(AuthorityKey -> NormalUser) { implicit request =>
    val user = loggedIn
    JsonData { res: OperatorPartData =>
      matchResult(operatorUpdate(res, user.id.get))
    }
  }

  def matchPasswords(pass: String) = StackAction(AuthorityKey -> NormalUser) { implicit request =>
    val user = loggedIn
    import org.mindrot.jbcrypt.BCrypt
    val hashPass = BCrypt.checkpw(pass, user.password)
    Ok(Json.toJson(Map("val" -> hashPass.asInstanceOf[Boolean])))
  }

  def uploadAvatar = StackAction(parse.multipartFormData, AuthorityKey -> NormalUser) { implicit request =>
    val user = loggedIn
    request.body.file("files[]").map { file =>
      val fname = file.filename
      val filename = "users/" + user.id.get + "/logo/avatar" + fname.substring(fname.indexOf("."), fname.length)
      val pic = new java.io.File(Play.application.path + "/public/upload/" + filename)
      
      file.ref.moveTo(pic, true)
      
      Ok(Json.toJson(
        Json.obj(
          "files" -> Json.arr(
            Json.obj(
              "name" -> filename,
              "size" -> pic.length,
              "url" -> filename,
              "thumbnail_url" -> "",
              "delete_url" -> "",
              "delete_type" -> ""
            )
          )
        )
      ))
    }.getOrElse {
      BadRequest(Json.toJson(Map("error" -> "Missing file")))
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
      case _ => JsonBad("No such type value, can't cast to a given type")
    }
    
  }

  private def JsonBad(x: String) = BadRequest(Json.toJson(Map("error" -> x.asInstanceOf[String])))

  private def checkOperator(gameId: Int)(operator: Operator): Boolean = findOperatorId(gameId) == operator.id.get

  private def authAndValidAction(authority: Authority, gid: Int)(f: User => Request[AnyContent] => Result) =
    StackAction(AuthorityKey -> authority) { implicit request =>
      val user = loggedIn
      if(checkOperator(gid)(user))
        f(user)(request)
      else
        authorizationFailed(request)
    }
}