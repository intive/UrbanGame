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
case class HalJsonRes(links: Seq[HalLink], embedded: Seq[HalJsonRes] = Seq(), body: JsValue = Json.obj())

class WebApi(auth: UserAuth, gamesService: GamesService) extends Controller {

  def root = ApiAction { implicit request =>
    val links = Seq(selfLink, gamesLink, loginLink, registerLink, userGamesLink)
    ApiOk(HalJsonRes(links))
  }

  def games(lat: Double, lon: Double) = ApiAction { implicit request =>
    val list = gamesService.listGames(lat, lon)
    val embedded = list map { game => 
      val selfLink = HalLink("self", gameLink(game.gid).href)
      val links = Seq(selfLink, gameStaticLink(game.gid), gameDynamicLink(game.gid))
      HalJsonRes(links, body=Json.toJson(game))
    }
    val links = Seq(selfLink, rootLink, loginLink, registerLink)
    ApiOk(HalJsonRes(links, embedded))
  }

  def game(gid: Int) = ApiUserAction { implicit request => user =>
    val links = Seq(selfLink, gameStaticLink(gid), gameDynamicLink(gid), gamesLink, tasksLink(gid))
    ApiOk(HalJsonRes(links))
  }

  def gameStatic(gid: Int) = ApiUserAction { implicit request => user =>
    val stat = gamesService.getGameStatic(gid)
    val links = Seq(selfLink, tasksLink(gid))
    ApiOk(HalJsonRes(links, body=Json.toJson(stat)))
  }

  def gameDynamic(gid: Int) = ApiUserAction { implicit request => user =>
    val links = Seq(selfLink)
    val dyna = gamesService.getGameDynamic(gid)
    ApiOk(HalJsonRes(links, body=Json.toJson(dyna)))
  }

  def tasks(gid: Int, lat: Double, lon: Double) = ApiUserAction { implicit request => user =>
    val list = gamesService.listTasks(user, gid, lat, lon)
    val embedded = list map { task => 
      val selfLink = HalLink("self", taskLink(task.gid, task.tid).href)
      val links = Seq(selfLink, userTaskStatusLink(task.gid, task.tid))
      HalJsonRes(links, body=Json.toJson(task))
    }
    val links = Seq(selfLink, gameLink(gid))
    ApiOk(HalJsonRes(links, embedded))
  }

  def task(gid: Int, tid: Int) = ApiUserAction { implicit request => user =>
    val links = Seq(selfLink, gameLink(gid), taskStaticLink(gid,tid), taskDynamicLink(gid,tid))
    ApiOk(HalJsonRes(links))
  }

  def taskStatic(gid: Int, tid: Int) = ApiUserAction { implicit request => user =>
    val stat = gamesService.getTaskStatic(gid, tid)
    val links = Seq(selfLink, gameLink(gid))
    ApiOk(HalJsonRes(links, body=Json.toJson(stat)))
  }

  def taskDynamic(gid: Int, tid: Int) = ApiUserAction { implicit request => user =>
    val dyna = gamesService.getTaskDynamic(gid, tid)
    val links = Seq(selfLink, gameLink(gid))
    ApiOk(HalJsonRes(links, body=Json.toJson(dyna)))
  }
  
  def login = ApiUserAction { implicit request => user =>
    val links = Seq(selfLink, gamesLink, userGamesLink, rootLink)
    ApiOk(HalJsonRes(links))
  }

  def register = ApiAction { implicit request =>
    withJsonArguments { a: RegisterArgs =>
      gamesService.createUser(a.login.trim(), a.password.trim())
      val links = Seq(selfLink, loginLink)
      ApiOk(HalJsonRes(links))
    }
  }

  def joinGame(gid: Int) = ApiUserAction { implicit request => user =>
    gamesService.joinGame(user, gid)
    val links = Seq(selfLink, userGameStatusLink(gid))
    ApiOk(HalJsonRes(links))
  }

  def leaveGame(gid: Int) = ApiUserAction { implicit request => user =>
    gamesService.leaveGame(user, gid)
    val links = Seq(selfLink, gamesLink)
    ApiOk(HalJsonRes(links))
  }

  def userGames() = ApiUserAction { implicit request => user =>
    val list = gamesService.listUserGames(user)
    val embedded = list map { game => 
      val selfLink = HalLink("self", gameLink(game.gid).href)
      val links = Seq(selfLink, tasksLink(game.gid), userGameStatusLink(game.gid))
      HalJsonRes(links, body=Json.toJson(game))
    }
    val links = Seq(selfLink, gamesLink)
    ApiOk(HalJsonRes(links, embedded))
  }

  def userGameStatus(gid: Int) = ApiUserAction { implicit request => user =>
      val status = gamesService.getUserGameStatus(user, gid)
      val links = Seq(selfLink, gameLink(gid))
      ApiOk(HalJsonRes(links, body=Json.toJson(status)))
  }
  
  def userTaskStatus(gid: Int, tid: Int) = ApiUserAction { implicit request => user =>
      val status = gamesService.getUserTaskStatus(user, gid, tid)
      val links = Seq(selfLink, gameLink(gid))
      ApiOk(HalJsonRes(links, body=Json.toJson(status)))
  }

  def sendUserAnswer(gid: Int, tid: Int) = ApiUserAction { implicit request => user =>
    val links = Seq(selfLink, gameLink(gid))
    ApiOk(HalJsonRes(links, body=Json.obj("info" -> "not implemented")))
  }

  private def ApiUserAction(f: Request[AnyContent] => User => Result): Action[AnyContent] =
    ApiAction { request => 
      try { f (request) (auth(request)) } catch {
        case authEx: AuthException => 
          ApiErr(authEx).withHeaders("WWW-Authenticate" -> "Basic realm=\"myrealm\"")
      }
    }

  private def ApiAction(f: Request[AnyContent] => Result): Action[AnyContent] = 
    Action { request =>
      //if (request.accepts("application/hal+json")) // it doesn't work
      if (true)
        try { f (request) } catch {
          case apiEx: ApiException => 
            ApiErr(apiEx)
          case ex: Exception => 
            InternalServerError(ex.getMessage)
        }
      else
        Status(406)("Client must accept 'application/hal+json' type")
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

  def ApiErr(apiEx: ApiException) = {
    val jsonMsg = Json.obj("code" -> apiEx.getCode, "message" -> apiEx.getMessage)
    Status(apiEx.getCode)(Json.prettyPrint(jsonMsg)).as("application/json")
  }

  case class GamesArgs(lat: Double, lon: Double, r: Double)
  case class RegisterArgs(login: String, password: String)

  implicit val gamesArgsReads    = Json.reads[GamesArgs]
  implicit val registerArgsReads = Json.reads[RegisterArgs]

  implicit val gpsTaskDetailsWrites  = Json.writes[GPSTaskDetails]
  implicit val abcTaskDetailsWrites  = Json.writes[ABCTaskDetails]
  implicit val taskSpecDetailsWrites = new Writes[TaskSpecDetails] {
    def writes(t: TaskSpecDetails): JsValue = t match {
      case gps: GPSTaskDetails => Json.toJson(gps)
      case abc: ABCTaskDetails => Json.toJson(abc)
    }
  }

  implicit val gameSummaryWrites = Json.writes[GameSummary]
  implicit val gameStaticWrites  = Json.writes[GameStatic]
  implicit val gameDynamicWrites = Json.writes[GameDynamic]
  implicit val gameStatusWrites  = Json.writes[UserGameStatus]
  implicit val taskSummaryWrites = Json.writes[TaskSummary]
  implicit val taskStaticWrites  = Json.writes[TaskStatic]
  implicit val taskDynamicWrites = Json.writes[TaskDynamic]
  implicit val taskStatusWrites  = Json.writes[UserTaskStatus]
  implicit val userGameSummary   = Json.writes[UserGameSummary]

  implicit val halJsonResWrites = new Writes[HalJsonRes] {
    def writes(r: HalJsonRes): JsValue = {
      val linksObj = Json.obj(  "_links" -> 
        r.links.map {l => 
          if (l.href contains '{') // uri looks like template
            Json.obj(l.name -> Json.obj("href" -> l.href, "templated" -> true))
          else
            Json.obj(l.name -> Json.obj("href" -> l.href))
        }.reduce {_ ++ _}
      )
      val embeddedObj = 
        if (r.embedded.isEmpty) Json.obj() 
        else Json.obj("_embedded" -> r.embedded.map {writes})
      return linksObj ++ embeddedObj ++ r.body.as[JsObject]
    }
  }

  val rootUrl = "/api"
  val gamesUrl = rootUrl + "/games"
  val loginUrl = rootUrl + "/login"
  val registerUrl = rootUrl + "/register"
  val userGamesUrl = rootUrl + "/my/games"

  val latLonParam = "{?lat,lon}"
  val rootLink = HalLink("root", rootUrl)
  val gamesLink = HalLink("games", gamesUrl+latLonParam)
  val loginLink = HalLink("login", loginUrl)
  val registerLink = HalLink("register", registerUrl)
  val userGamesLink = HalLink("userGames", userGamesUrl)
  def selfLink(implicit r: Request[AnyContent]) = HalLink("self", r.path)
  def gameLink(gid: Int) = HalLink("game", s"$gamesUrl/$gid")
  def gameStaticLink(gid: Int) = HalLink("gameStatic", s"$gamesUrl/$gid/static")
  def gameDynamicLink(gid: Int) = HalLink("gameDynamic", s"$gamesUrl/$gid/dynamic")
  def tasksLink(gid: Int) = HalLink("tasks", s"$gamesUrl/$gid/tasks"+latLonParam)
  def taskLink(gid: Int, tid: Int) = HalLink("task", s"$gamesUrl/$gid/tasks/$tid")
  def taskStaticLink(gid: Int, tid: Int) = HalLink("taskStatic", s"$gamesUrl/$gid/tasks/$tid/static")
  def taskDynamicLink(gid: Int, tid: Int) = HalLink("taskDynamic", s"$gamesUrl/$gid/tasks/$tid/dynamic")
  def userGameStatusLink(gid: Int) = HalLink("userGameStatus", s"$userGamesUrl/$gid")
  def userTaskStatusLink(gid: Int, tid: Int) = HalLink("userTaskStatus", s"$userGamesUrl/$gid/tasks/$tid")
}

