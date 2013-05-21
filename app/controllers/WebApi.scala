/**Copyright 2013 BLStream, BLStream's Patronage Program Contributors
 * 		 http://blstream.github.com/UrbanGame/
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 		 http://www.apache.org/licenses/LICENSE-2.0
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

import webapi.models._

import play.api.libs.json._
import play.api.libs.functional.syntax._

class WebApi(auth: UserAuth, gamesService: GamesService) extends Controller {

  def root = ApiAction { request =>
    Ok("Hello!")
  }

  def games(lat: Double, lon: Double, r: Double) = ApiAction { request =>
    val list = gamesService.listGames(lat, lon, r)
    Ok(Json.toJson(list))
  }

  def login = ApiUserAction { request => user =>
    Ok("You are logged!")
  }

  def register = ApiAction { implicit request =>
    withJsonArguments { a: RegisterArgs =>
      gamesService.createUser(a.login, a.password)
      Ok(s"Created user ${a.login}!")
    }
  }

  def userGames = ApiUserAction { request => user =>
    val list = gamesService.listGames(0,0,0)
    Ok(Json.toJson(list))
  }

  def gameStatic(gid: Int) = ApiUserAction { request => user =>
    val stat = gamesService.gameStatic(gid)
    stat map { x => Ok(Json.toJson(x)) } getOrElse NotFound("Game not found")
  }

  def gameDynamic(gid: Int) = ApiUserAction { request => user =>
    val dyna = gamesService.gameDynamic(gid)
    dyna map { x => Ok(Json.toJson(x)) } getOrElse NotFound("Game not found")
  }
  
  private def ApiUserAction(f: Request[AnyContent] => User => Result): Action[AnyContent] =
    ApiAction { request => 
      try { f (request) (auth(request)) } catch {
        case authEx: AuthException => 
          Unauthorized(authEx.getMessage).withHeaders("WWW-Authenticate" -> "Basic realm=\"myrealm\"")
      }
    }

  private def ApiAction(f: Request[AnyContent] => Result): Action[AnyContent] = 
    Action { request =>
      //if (request.accepts("application/hal+json")) // it doesn't work
      if (true)
        try { f (request) } catch {
          case ex: Exception => 
            InternalServerError(ex.getMessage)
        }
      else
        BadRequest("Client must accept 'application/hal+json' type")
    }

  private def withJsonArguments[A] (f: A => Result) (implicit request: Request[AnyContent], rds: Reads[A]) = {
    request.body.asJson map { json =>
      json.validate (rds) map (f) recoverTotal {e => BadRequest(JsError.toFlatJson(e))}
    } getOrElse {
      BadRequest("Expected Json arguments")
    }
  }

  case class GamesArgs(lat: Double, lon: Double, r: Double)
  case class RegisterArgs(login: String, password: String)

  implicit val gamesArgsReads    = Json.reads[GamesArgs]
  implicit val registerArgsReads = Json.reads[RegisterArgs]

  implicit val userWritesWrites  = Json.writes[User]
  implicit val gameSummaryWrites = Json.writes[GameSummary]
  implicit val gameStaticWrites  = Json.writes[GameStatic]
  implicit val gameDynamicWrites = Json.writes[GameDynamic]
}

