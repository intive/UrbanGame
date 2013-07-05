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
import play.api.data.validation.Constraints._
import play.api.Play.current
import jp.t2v.lab.play2.auth._
import play.api.cache.Cache
import scala.util.{ Try, Success, Failure }
import models.utils._
import models.dal.Bridges._

object Application extends Controller with CookieLang with OptionalAuthElement with LoginLogout with AuthConfigImpl with EmailConfirmation {
    
  def lan(implicit request: RequestHeader) = request.cookies.get("PLAY_LANG").getOrElse(Cookie("PLAY_LANG", lang.code)).value.toString

  def index = StackAction { implicit request =>
    val user: Option[User] = loggedIn
    Ok(Scalate("index").render('title -> "Urban Game", 'user -> user, 'loginForm -> loginForm, 'registrationForm -> registrationForm))
  }

  def login = StackAction { implicit request =>
    val user: Option[User] = loggedIn
    user match {
      case Some(u) => Redirect(request.headers.get(REFERER).getOrElse(routes.GamesCtrl.myGames.url))
      case _ => Ok(Scalate("login").render('title -> "Urban Game - Login", 'user -> None, 'loginForm -> loginForm))
    }
  }

  def authenticate = Action { implicit request =>
    loginForm.bindFromRequest.fold(
      errors => BadRequest(Scalate("login").render('title -> "Urban Game", 'user -> None, 'loginForm -> errors)),
      { case (remember, user) =>
          if(user.get.validated)
            gotoLoginSucceeded(user.get.id.get) 
          else {
            val msg = Messages("notify.login.novalidated", routes.Application.newToken("signup", user.get.email))(Lang(lan))
            Ok(Scalate("confirmations").render('title -> "Urban Game - E-mail confirmation", 'user -> None, 
              'message -> msg))
          }
      }
    )
  }

  def logout = Action { implicit request =>
    gotoLogoutSucceeded.flashing(
      "success" -> Messages("notify.logout.msg")(Lang(lan))
    )
  }

  import play.api.db.slick.Config.driver.simple._
  import play.api.libs.json._
  import play.api.libs.functional.syntax._
  import java.util.UUID
  
  def register = StackAction { implicit request =>
    registrationForm.bindFromRequest.fold(
      errors => BadRequest(Scalate("index").render('title -> "Urban Game", 'user -> None, 'registrationForm -> errors)),
      op => {
        val msg = operatorSave(op) match {
          case Success(a) => sendEmailToken(op.email) match {
            case false => Messages("notify.register.notSent")(Lang(lan))
            case _ => Messages("notify.register.sent", op.email)(Lang(lan))
          }
          case Failure(e) => Messages("notify.register.dbIssue")(Lang(lan))
        }
        
        Redirect(routes.Application.notification).flashing(
          "notification" -> msg
        )
      }
    )
  }

  def notification = StackAction { implicit request =>
    val user = loggedIn
    flash.get("notification") match {
      case Some(msg) => Ok(Scalate("confirmations").render('title -> "Urban Game - E-mail confirmation", 'user -> user, 'message -> msg))
      case None => Redirect(routes.Application.index)
    }
  }

  def newToken(typ: String, email: String) = StackAction { implicit request =>
    typ match {
      case "signup" => sendEmailToken(email)
      case "password" => //TODO
    }

    Redirect(routes.Application.login)
  }

  def confirm(email: String, token: String) = StackAction { implicit request =>
    val user: Option[User] = loggedIn
    val op = findOperatorByEmail(email)

    val msg: String = true match {
      case _ if(op == None) => Messages("notify.confirm.noUser")(Lang(lan))
      case _ if(op.get.validated) => Messages("notify.confirm.alreadyValidated")(Lang(lan))
      case _ if(op.get.token.get == token) => updateSignUpToken(op.get.email, None) match {
        case Success(a) => Messages("notify.confirm.okToken")(Lang(lan))
        case Failure(e) => Messages("notify.confirm.dbIssue")(Lang(lan))
      }
      case _ if(op.get.token.get != token) => Messages("notify.confirm.badToken", routes.Application.newToken("signup", op.get.email))(Lang(lan))
    }

    Ok(Scalate("confirmations").render('title -> "Urban Game - E-mail confirmation", 'user -> user, 'message -> msg))
  }

  def checkEmail(email: String) = StackAction { implicit request =>
    val res = findOperatorByEmail(email) match {
      case Some(x) => false
      case None => true
    }

    Ok(Json.toJson(Map("val" -> res)))
  }

  def passwordRecovery = StackAction { implicit request =>
    val user: Option[User] = loggedIn

    Ok(Scalate("passrecovery").render('title -> "Urban Game - Password recovery", 'user -> user, 'passRecForm -> recovery))
  }

  def recover = StackAction { implicit request =>
    val user: Option[User] = loggedIn
    recovery.bindFromRequest.fold(
      errors => BadRequest(Scalate("passrecovery").render('title -> "Urban Game", 'user -> user, 'passRecForm -> errors)),
      email => {
        val msg = findOperatorByEmail(email) match {
          case Some(op) => {
            val password = UUID.randomUUID.toString.substring(0,8)
            val opc = op.copy(password = org.mindrot.jbcrypt.BCrypt.hashpw(password, org.mindrot.jbcrypt.BCrypt.gensalt()))
            operatorUpdate(opc) match {
              case Success(a) => {
                sendNewPassword(opc.email, password, request)
                Messages("notify.password.recover.send")(Lang(lan))
              }
              case Failure(e) => Messages("notify.password.recover.notsend")(Lang(lan)) + "<br><br>Reason: " + e.toString
            }
          }
          case None => Messages("notify.password.recover.notsend")(Lang(lan)) + "<br><br><span class='bold'>Reason:</span> " + Messages("notify.password.recover.notsend.nouser", email)(Lang(lan))
        }
        
        Redirect(routes.Application.notification).flashing(
          "notification" -> msg
        )
      }
    )
  }

  def fillDatabase = Action { implicit request =>
    import scala.io._
    import com.github.nscala_time.time.Imports._

    val filepaths: List[String] = List("app/initData/games.txt","app/initData/operators.txt")
    var cnt1 = 0
    var cnt2 = 0
    var cnt3 = 0
        
    play.api.db.slick.DB.withSession { implicit session =>
      if (Operators.findAll.isEmpty) {
        Source.fromFile(filepaths(1)).getLines.foreach { line => 
          val List(email, pass, name, permission) = line.split("::").map(_.toString).toList

          val od = Operator(id = None, email = email, password = pass, name = name, 
            permission = Permission.valueOf(permission), validated = true, token = None)

          Operators.create(od)
          cnt2 = cnt2 + 1
        }
      }

      if (Games.findAll.isEmpty) {
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

      if (Tasks.findAll.isEmpty) {
        val tasks = Seq(
          TasksDetails(
            Some(0), 1, 1, "GPS", "Zadanie 1", "Idź do ZOO", 10,
            1, 1000000, None, None, None, None, true, 0
          ),
          TasksDetails(
            Some(1), 1, 1, "GPS", "Zadanie 2", "Idź na stadion miejski", 10,
            1, 1000000, None, None, None, None, true, 0
          ),
          TasksDetails(
            Some(2), 1, 1, "ABC", "Zadanie 3", "Które zdanie jest prawdziwe?", 20,
            1, 1000000, None, None, None, None, true, 0
          ),
          TasksDetails(
            Some(3), 1, 1, "ABC", "Zadanie 4", "Odczytaj kody za szybą BLStreamowego auta", 40,
            1, 1000000, None, None, None, None, true, 0
          )
        )

        val gps = Seq (
          GPSTask(1, 0, 0, 51.14212, 16.94169, 1000),
          GPSTask(1, 1, 0, 51.07153, 17.00637, 1000)
        )

        val abc = Seq (
          ABCTaskOption(1, 2, "a", "1 + 1 = 2", 10),
          ABCTaskOption(1, 2, "b", "1 + 1 = 10", 10),
          ABCTaskOption(1, 2, "c", "1 + 1 = 3", 0),
          ABCTaskOption(1, 2, "d", "1 + 1 = 14", -5),

          ABCTaskOption(1, 3, "a", "690c6f2b", -10),
          ABCTaskOption(1, 3, "b", "29f7c98d", -10),
          ABCTaskOption(1, 3, "c", "76233bbb", -10),
          ABCTaskOption(1, 3, "d", "891cc1ce",  20),
          ABCTaskOption(1, 3, "e", "82e3028a",  10),
          ABCTaskOption(1, 3, "f", "1c410a35",  10),
          ABCTaskOption(1, 3, "g", "d2630a28",  -10),
          ABCTaskOption(1, 3, "h", "64de15b3",  -10),
          ABCTaskOption(1, 3, "i", "d3e7117a",  -10),
          ABCTaskOption(1, 3, "j", "ac5d4b79",  -10)
        )

        models.Tasks.insertAll (tasks: _*)
        models.GPSTasks.insertAll (gps: _*)
        models.ABCTasks.insertAll (abc: _*)
        cnt3 = tasks size
      }
    }

    Ok("Inserted " + cnt1 + " game(s) and " + cnt2 + " operator(s) and " + cnt3 + " task(s)")
  }

  def jsMessages = Action { implicit request =>
    import jsmessages.api.JsMessages
    Ok(JsMessages.apply(Some("Messages"))).as(JAVASCRIPT)
  }

  def registrationForm(implicit request: RequestHeader) = Form {
    mapping(
      "name" -> text.verifying("error.name.required", res => res.nonEmpty),
      "email" -> email.verifying("error.email.exists", res => findOperatorByEmail(res) == None),
      "password" -> tuple(
        "main" -> text(minLength = 6),
        "confirm" -> text.verifying("error.name.required", res => res.nonEmpty)
      ).verifying("error.passwords.match", passwords => passwords._1 == passwords._2),
      "captcha" -> tuple(
        "recaptcha_challenge_field" -> text.verifying("error.captcha.required", res => res.nonEmpty),
        "recaptcha_response_field" -> text.verifying("error.captcha.required", res => res.nonEmpty)
      ).verifying("error.captcha.match", captcha => Recaptcha.validate(captcha._1, captcha._2)),
      "accept" -> checked("error.accept.therms")
    ) {
      case (name, email, passwords, captcha, accept) => Operator(id = None, email = email, password = passwords._1, name = name)
    } {
      operator => Some(operator.name, operator.email, (operator.password, ""), ("", ""), false)
    }
  }

  val loginForm = Form {
    tuple(
      "remember" -> boolean,
      "user" -> mapping(
        "email" -> email, 
        "password" -> text.verifying("error.password.required", res => res.nonEmpty)
      )(auth)(_.map(u => (u.email, "")))
        .verifying("error.login.invalid.data", result => result.isDefined)
    )
  }

  val recovery = Form {
    single(
      "email" -> email
    )
  }

  private def sendEmailToken(email: String)(implicit request: RequestHeader) = {
    val token = UUID.randomUUID.toString
    updateSignUpToken(email, Some(token)) match {
      case Success(a) => sendSignUpConfirmation(email, token, request)
      case Failure(e) => false
    }
  }

}
