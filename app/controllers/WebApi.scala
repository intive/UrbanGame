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

case class HalLink(name: String, href: String) {
  def asSelf = HalLink("self", href)
}
case class HalJsonRes(links: Seq[HalLink], embedded: Seq[HalJsonRes] = Seq(), body: JsValue = Json.obj())

class WebApi(auth: UserAuth, gamesService: GamesService) extends Controller {

  def root = ApiAction { implicit request =>
    val links = Seq(selfLink, gamesLink, loginLink, registerLink, userGamesLink)
    ApiOk(HalJsonRes(links))
  }

  def games(lat: Double, lon: Double) = ApiAction { implicit request =>
    val list = gamesService.listGames(lat, lon)
    val embedded = list map { game => 
      val selfLink = gameLink(game.gid).asSelf
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
      val selfLink = taskLink(gid, task.tid).asSelf
      val links = Seq(selfLink, taskStaticLink(gid, task.tid), taskDynamicLink(gid, task.tid))
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
      val selfLink = gameLink(game.gid).asSelf
      val links = Seq(selfLink, tasksLink(game.gid), userGameStatusLink(game.gid))
      HalJsonRes(links, body=Json.toJson(game))
    }
    val links = Seq(selfLink, gamesLink)
    ApiOk(HalJsonRes(links, embedded))
  }

  def userGameStatus(gid: Int) = ApiUserAction { implicit request => user =>
      val status = gamesService.getUserGameStatus(user, gid)
      val links = Seq(selfLink)
      ApiOk(HalJsonRes(links, body=Json.toJson(status)))
  }
  
  def userTaskStatus(gid: Int, tid: Int) = ApiUserAction { implicit request => user =>
      val status = gamesService.getUserTaskStatus(user, gid, tid)
      val links = Seq(selfLink)
      ApiOk(HalJsonRes(links, body=Json.toJson(status)))
  }

  def sendUserAnswer(gid: Int, tid: Int) = ApiUserAction { implicit request => user =>
    withJsonArguments { a: UserAnswer =>
      val status = gamesService.checkUserAnswer(user, gid, tid, a)
      val embedded = Seq(
        HalJsonRes(Seq(userTaskStatusLink(gid,tid).asSelf), body=Json.toJson(status))
      )
      val links = Seq(selfLink, userTaskStatusLink(gid, tid))
      ApiOk(HalJsonRes(links, embedded))
    }
  }

  private def ApiUserAction(f: Request[AnyContent] => User => Result): Action[AnyContent] =
    ApiAction { implicit request => 
      try { f (request) (auth(request)) } catch {
        case authEx: AuthException => 
          ApiErr(authEx).withHeaders("WWW-Authenticate" -> "Basic realm=\"myrealm\"")
      }
    }

  private def ApiAction(f: Request[AnyContent] => Result): Action[AnyContent] = 
    Action { implicit request =>
      try { f (request) } catch {
        case apiEx: ApiException => 
          ApiErr(apiEx)
        case ex: Exception => 
          ApiErr(unexpectedError(ex.getMessage))
      }
    }

  private def withJsonArguments[A] (f: A => Result) (implicit request: Request[AnyContent], rds: Reads[A]) = {
    request.body.asJson map { json =>
      json.validate (rds) map (f) recoverTotal {e => BadRequest(Json.obj("code" -> 400, "message" -> JsError.toFlatJson(e)))}
    } getOrElse {
      ApiErr(jsonExpected)
    }
  }

  private def ApiOk(response: HalJsonRes) = {
    Ok(Json.prettyPrint(Json.toJson(response))).as("application/json")
  }

  import play.api.i18n.Messages
  private def ApiErr(apiEx: ApiException)(implicit request: Request[Any]) = {
    val msg = Messages(apiEx.getMessage, apiEx.getParams: _*)
    val jsonMsg = Json.obj("code" -> apiEx.getCode, "message" -> msg)
    Status(apiEx.getCode)(Json.prettyPrint(jsonMsg)).as("application/json")
  }

  private case class RegisterArgs(login: String, password: String)

  private implicit val registerArgsReads = Json.reads[RegisterArgs]
  private implicit val userAnswerReads   = Json.reads[UserAnswer]
  private implicit val ABCOptionWrites   = Json.writes[ABCOption]
  private implicit val gameSummaryWrites = Json.writes[GameSummary]
  private implicit val gameStaticWrites  = Json.writes[GameStatic]
  private implicit val gameDynamicWrites = Json.writes[GameDynamic]
  private implicit val gameStatusWrites  = Json.writes[UserGameStatus]
  private implicit val taskDynamicWrites = Json.writes[TaskDynamic]
  private implicit val userGameSummaryWrites = Json.writes[UserGameSummary]
  private implicit val userTaskStatusWrites  = Json.writes[UserTaskStatus]
  private implicit val taskSummaryWrites = (
    (__ \ "gid").write[Int] ~
    (__ \ "tid").write[Int] ~
    (__ \ "version").write[Int] ~
    (__ \ "name").write[String] ~
    (__ \ "type").write[String]
  )(unlift(TaskSummary.unapply))
  private implicit val taskStaticWrites = (
    (__ \ "gid").write[Int] ~
    (__ \ "tid").write[Int] ~
    (__ \ "version").write[Int] ~
    (__ \ "type").write[String] ~
    (__ \ "name").write[String] ~
    (__ \ "description").write[String] ~
    (__ \ "choices").write[Option[List[ABCOption]]] ~
    (__ \ "maxpoints").write[Int] ~
    (__ \ "maxattempts").write[Int]
  )(unlift(TaskStatic.unapply))
  import com.github.nscala_time.time.Imports._
  private implicit val userTaskSummaryWrites = (
    (__ \ "gid").write[Int] ~
    (__ \ "tid").write[Int] ~
    (__ \ "version").write[Int] ~
    (__ \ "type").write[String] ~
    (__ \ "deadline").write[DateTime]
  )(unlift(UserTaskSummary.unapply))

  private implicit val halJsonResWrites = new Writes[HalJsonRes] {
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

  private val rootUrl = "/api"
  private val gamesUrl = rootUrl + "/games"
  private val loginUrl = rootUrl + "/login"
  private val registerUrl = rootUrl + "/register"
  private val userGamesUrl = rootUrl + "/my/games"

  private val latLonParam = "{?lat,lon}"
  private val rootLink = HalLink("root", rootUrl)
  private val gamesLink = HalLink("games", gamesUrl+latLonParam)
  private val loginLink = HalLink("login", loginUrl)
  private val registerLink = HalLink("register", registerUrl)
  private val userGamesLink = HalLink("userGames", userGamesUrl)
  private def selfLink(implicit r: Request[AnyContent]) = HalLink("self", r.path)
  private def gameLink(gid: Int) = HalLink("game", s"$gamesUrl/$gid")
  private def gameStaticLink(gid: Int) = HalLink("gameStatic", s"$gamesUrl/$gid/static")
  private def gameDynamicLink(gid: Int) = HalLink("gameDynamic", s"$gamesUrl/$gid/dynamic")
  private def tasksLink(gid: Int) = HalLink("tasks", s"$gamesUrl/$gid/tasks"+latLonParam)
  private def taskLink(gid: Int, tid: Int) = HalLink("task", s"$gamesUrl/$gid/tasks/$tid")
  private def taskStaticLink(gid: Int, tid: Int) = HalLink("taskStatic", s"$gamesUrl/$gid/tasks/$tid/static")
  private def taskDynamicLink(gid: Int, tid: Int) = HalLink("taskDynamic", s"$gamesUrl/$gid/tasks/$tid/dynamic")
  private def userGameStatusLink(gid: Int) = HalLink("userGameStatus", s"$userGamesUrl/$gid")
  private def userTaskStatusLink(gid: Int, tid: Int) = HalLink("userTaskStatus", s"$userGamesUrl/$gid/tasks/$tid")

  private val jsonExpected = new ApiException(400, "webapi.error.jsonExpected")
  private def unexpectedError(msg: String) = new ApiException(500, "webapi.error.unexpectedError", Seq(msg))
}

