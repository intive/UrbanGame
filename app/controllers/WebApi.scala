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

case class HalLink(name: String, href: String)
case class HalJsonRes(links: Seq[HalLink], embedded: Seq[HalJsonRes] = Seq(), values: JsValue = Json.obj())

class WebApi(auth: UserAuth, gamesService: GamesService) extends Controller {

  def root = ApiAction { implicit request =>
    val links = Seq(selfLink, gamesLink, loginLink, registerLink, userGamesLink)
    ApiOk(HalJsonRes(links))
  }

  def games(lat: Double, lon: Double, r: Double) = ApiAction { implicit request =>
    val links = Seq(selfLink, gamesLink)
    val list = gamesService.listGames(lat, lon, r)
    val embedded = list map { game => 
      val selfHref = gameLink(game.gid).href
      val links = Seq(HalLink("self", selfHref))
      HalJsonRes(links, Seq(), Json.toJson(game))
    }
    ApiOk(HalJsonRes(links, embedded))
  }

  def login = ApiUserAction { implicit request => user =>
    val links = Seq(selfLink, gamesLink, userGamesLink, rootLink)
    ApiOk(HalJsonRes(links))
  }

  def register = ApiAction { implicit request =>
    withJsonArguments { a: RegisterArgs =>
      val links = Seq(selfLink, loginLink)
      gamesService.createUser(a.login, a.password)
      ApiOk(HalJsonRes(links))
    }
  }

  def userGames() = ApiUserAction { implicit request => user =>
    val links = Seq(selfLink, gamesLink)
    val list = gamesService.listUserGames(user)
    val embedded = list map { game => 
      val selfHref = gameLink(game.gid).href
      val links = Seq(HalLink("self", selfHref))
      HalJsonRes(links, Seq(), Json.toJson(game))
    }
    ApiOk(HalJsonRes(links, embedded))
  }

  def game(gid: Int) = ApiUserAction { implicit request => user =>
    val links = Seq(selfLink, gameStaticLink(gid), gameDynamicLink(gid), gamesLink, tasksLink(gid))
    ApiOk(HalJsonRes(links))
  }

  def gameStatic(gid: Int) = ApiUserAction { implicit request => user =>
    val stat = gamesService.gameStatic(gid)
    stat map { x => 
      val links = Seq(selfLink, gameLink(gid))
      ApiOk(HalJsonRes(links, Seq(), Json.toJson(stat)))
    } getOrElse NotFound("Game not found")
  }

  def gameDynamic(gid: Int) = ApiUserAction { implicit request => user =>
    val dyna = gamesService.gameDynamic(gid)
    dyna map { x => 
      val links = Seq(selfLink, gameLink(gid))
      ApiOk(HalJsonRes(links, Seq(), Json.toJson(dyna)))
    } getOrElse NotFound("Game not found")
  }

  def userGameStatus(gid: Int) = ApiUserAction { implicit request => user =>
      val links = Seq(selfLink, gameLink(gid))
      val status = gamesService.getUserGameStatus(user, gid)
      status map {
        s => ApiOk(HalJsonRes(links, Seq(), Json.toJson(s)))
      } getOrElse NotFound("Game not found")
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

  def ApiOk(response: HalJsonRes) = {
    Ok(Json.prettyPrint(Json.toJson(response))).as("application/json")
  }

  case class GamesArgs(lat: Double, lon: Double, r: Double)
  case class RegisterArgs(login: String, password: String)

  implicit val gamesArgsReads    = Json.reads[GamesArgs]
  implicit val registerArgsReads = Json.reads[RegisterArgs]

  implicit val userWritesWrites  = Json.writes[User]
  implicit val gameSummaryWrites = Json.writes[GameSummary]
  implicit val gameStaticWrites  = Json.writes[GameStatic]
  implicit val gameDynamicWrites = Json.writes[GameDynamic]
  implicit val gameStatusWrites  = Json.writes[GameStatus]

  implicit val halJsonResWrites = new Writes[HalJsonRes] {
    def writes(r: HalJsonRes): JsValue = {
      val linksObj = Json.obj(  "_links" -> 
        r.links.map {l => Json.obj(l.name -> Json.obj("href" -> l.href))}.reduce {_ ++ _}
      )
      val embeddedObj = 
        if (r.embedded.isEmpty) Json.obj() 
        else Json.obj("_embedded" -> r.embedded.map {writes})
      return linksObj ++ embeddedObj ++ r.values.as[JsObject]
    }
  }

  val rootUrl = "/api"
  val gamesUrl = rootUrl + "/games"
  val loginUrl = rootUrl + "/login"
  val registerUrl = rootUrl + "/register"
  val userGamesUrl = rootUrl + "/my/games"

  val rootLink = HalLink("root", rootUrl)
  val gamesLink = HalLink("games", gamesUrl)
  val loginLink = HalLink("login", loginUrl)
  val registerLink = HalLink("register", registerUrl)
  val userGamesLink = HalLink("userGames", userGamesUrl)
  def selfLink(implicit r: Request[AnyContent]) = HalLink("self", r.path)
  def gameLink(gid: Int) = HalLink("game", s"$gamesUrl/$gid")
  def gameStaticLink(gid: Int) = HalLink("gameStatic", s"$gamesUrl/$gid/static")
  def gameDynamicLink(gid: Int) = HalLink("gameDynamic", s"$gamesUrl/$gid/dynamic")
  def tasksLink(gid: Int) = HalLink("tasks", s"$gamesUrl/$gid/tasks")
}

