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
import models.mutils._
import models.dal.Bridges._
import play.api.data._
import play.api.data.Forms._

object Application extends Controller with CookieLang {
  
  def index = Action { implicit request =>
    Ok(Scalate("index").render('title -> "Urban Game"))
  }

  val loginForm = Form(
    tuple(
      "login" -> nonEmptyText,
      "password" -> nonEmptyText
    )
  )

  def login = Action { implicit request =>
    loginForm.bindFromRequest.fold(
      errors => BadRequest(Scalate("index").render('title -> "Urban Game", 'errors -> errors)),
      { case (login, password) => 
          Redirect(routes.GamesCtrl.mygames)
      }
    )
  }

  def logout = Action {
    Ok(Scalate("logout").render('title -> "Urban Game - Logout"))
  }

  import play.api.libs.json._
  import play.api.libs.functional.syntax._
  
  def register = Action { implicit request =>
    val od = OperatorsData(None, "op", "pass")

    val opId = play.api.db.slick.DB.withSession { implicit session =>
      Operators.createAccount(od)
    }

    Ok(Json.toJson(opId))
  }

  def dummyTestFunction(left: Int,right: Int):Int=left+right

}