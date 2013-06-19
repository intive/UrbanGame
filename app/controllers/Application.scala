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
import play.api.i18n._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.Play.current
import jp.t2v.lab.play2.auth._
import play.api.cache.Cache
import scala.util.{ Try, Success, Failure }
import models.utils._
import models.dal.Bridges._

object Application extends Controller with CookieLang with OptionalAuthElement with LoginLogout with RememberMeElement with EmailConfirmation {
  
  def index = StackAction { implicit request =>
    val user: Option[User] = loggedIn
    Ok(Scalate("index").render('title -> "Urban Game", 'user -> user))
  }

  val loginForm = Form {
    tuple(
      "remember" -> boolean,
      "user" -> mapping(
        "email" -> email, 
        "password" -> text
      )(auth)(_.map(u => (u.email, "")))
        .verifying("Invalid email or password", result => result.isDefined)
    )
  }

  def login = StackAction { implicit request =>
    val user: Option[User] = loggedIn
    user match {
      case Some(u) => Redirect(request.headers.get(REFERER).getOrElse(routes.GamesCtrl.myGames.url))
      case _ => Ok(Scalate("login").render('title -> "Urban Game - Login", 'user -> None))
    }
  }

  def authenticate = Action { implicit request =>
    loginForm.bindFromRequest.fold(
      errors => {Console.printf(errors.toString)
        BadRequest(Scalate("login").render('title -> "Urban Game", 'errors -> errors, 'user -> None))},
      { case (remember, user) => {
        if(remember) 
          gotoLoginSucceeded(user.get.id.get).withCookies(Cookie(persistentCookieName, 
            play.api.libs.Crypto.sign(user.get.email), Some(persistentSessionTimeout)))
        else
          gotoLoginSucceeded(user.get.id.get) 
        }
      }
    )
  }

  def logout = Action { implicit request =>
    gotoLogoutSucceeded.flashing(
      "success" -> "You've been logged out"
    )
  }

  import play.api.db.slick.Config.driver.simple._
  import play.api.libs.json._
  import play.api.libs.functional.syntax._
  import java.util.UUID
  
  def register = StackAction { implicit request =>
    val user: Option[User] = loggedIn
    val lan = request.cookies.get("PLAY_LANG").getOrElse(Cookie("PLAY_LANG", lang.code)).value.toString
    val op = Operator(id = None, email = "anesq@gazeta.pl", password = "pass", 
      name = "Name1", permission = NormalUser, token = None)

    val msg = operatorSave(op) match {
      case Success(a) => sendToken(op.email, request) match {
        case false => Messages("register.notSent")(Lang(lan))
        case _ => Messages("register.sent", op.email)(Lang(lan))
      }
      case Failure(e) => Messages("register.dbIssue")(Lang(lan))
    }

    Redirect(routes.Application.notification).flashing(
      "notification" -> msg
    )
  }

  def notification = StackAction { implicit request =>
    val user = loggedIn
    flash.get("notification") match {
      case Some(msg) => Ok(Scalate("confirmations").render('title -> "Urban Game - E-mail confirmation", 'user -> user, 'message -> msg))
      case None => Redirect(routes.Application.index)
    }
  }

  def confirm(email: String, token: String) = StackAction { implicit request =>
    val user: Option[User] = loggedIn
    val lan = request.cookies.get("PLAY_LANG").getOrElse(Cookie("PLAY_LANG", lang.code)).value.toString
    val op = findByEmail(email)

    val msg: String = true match {
      case _ if(op == None) => Messages("confirm.noUser")(Lang(lan))
      case _ if(op.get.validated) => Messages("confirm.alreadyValidated")(Lang(lan))
      case _ if(op.get.token.get == token) => updateSignUpToken(op.get.email, None) match {
        case Success(a) => Messages("confirm.okToken")(Lang(lan))
        case Failure(e) => Messages("confirm.dbIssue")(Lang(lan))
      }
      case _ if(op.get.token.get != token) => {
        sendToken(op.get.email, request)
        Messages("confirm.badToken")(Lang(lan))
      }
    }

    Ok(Scalate("confirmations").render('title -> "Urban Game - E-mail confirmation", 'user -> user, 'message -> msg))
  }

  def fillDatabase = Action { implicit request =>
    import scala.io._
    import com.github.nscala_time.time.Imports._

    val filepaths: List[String] = List("app/initData/games.txt","app/initData/operators.txt")
    var cnt1 = 0
    var cnt2 = 0
    var cnt3 = 0
        
    play.api.db.slick.DB.withSession { implicit session =>
      if (Operators.getRowsNo == 0) {
        Source.fromFile(filepaths(1)).getLines.foreach { line => 
          val List(email, pass, name, permission) = line.split("::").map(_.toString).toList

          val od = Operator(id = None, email = email, password = pass, name = name, 
            permission = Permission.valueOf(permission), validated = true, token = None)

          Operators.create(od)
          cnt2 = cnt2 + 1
        }
      }

      if (Games.getRowsNo == 0) {
        Source.fromFile(filepaths(0)).getLines.foreach { line => 
          val List(name, version, description, location, operatorId, created, startTime, 
            endTime, started, ended, winning, nWins, difficulty, maxPlayers, awards, 
            status, image) = line.split("::").map(_.toString).toList

          val gd = GamesDetails(None, name, version.toInt, description, location, operatorId.toInt, new DateTime(created), 
            DateTime.now, new DateTime(startTime), new DateTime(endTime), Some(new DateTime(started)), Some(new DateTime(ended)), 
            winning, nWins.toInt, difficulty, maxPlayers.toInt, awards, status, image)

          Games.create(gd)
          cnt1 = cnt1 + 1
        }
      }

      if (Tasks.getRowsNo == 0) {
        val td = TasksDetails(None, 1, 1, "Task1", "Task1 desc", DateTime.now + 2.days, 100, 20)

        Tasks.createTask(td)
        cnt3 = cnt3 + 1
      }
    }

    Ok("Inserted " + cnt1 + " game(s) and " + cnt2 + " operator(s) and " + cnt3 + " task(s)")
  }

  def jsMessages = Action { implicit request =>
    import jsmessages.api.JsMessages
    Ok(JsMessages.apply(Some("Messages"))).as(JAVASCRIPT)
  }

  private def sendToken(email: String, request: RequestHeader) = {
    val token = UUID.randomUUID.toString
    updateSignUpToken(email, Some(token)) match {
      case Success(a) => sendSignUpConfirmation(email, token, request)
      case Failure(e) => false
    }
  }

}