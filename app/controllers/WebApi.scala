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

  def games = ApiAction { request =>
    val list = gamesService.listGames(0,0,0)
    Ok(Json.toJson(list))
  }

  def login = UserAction { request => user =>
    Ok("You are logged!")
  }

  def userGames = UserAction { request => user =>
    val list = gamesService.listGames(0,0,0)
    Ok(Json.toJson(list))
  }

  def gameStatic(gid: Int) = UserAction { request => user =>
    val stat = gamesService.gameStatic(gid)
    stat map { x => Ok(Json.toJson(x)) } getOrElse NotFound("Game not found")
  }

  def gameDynamic(gid: Int) = UserAction { request => user =>
    val dyna = gamesService.gameDynamic(gid)
    dyna map { x => Ok(Json.toJson(x)) } getOrElse NotFound("Game not found")
  }
  
  private def UserAction(f: Request[AnyContent] => User => Result): Action[AnyContent] =
    ApiAction { request => f (request) (auth(request)) }

  private def ApiAction(f: Request[AnyContent] => Result): Action[AnyContent] = 
    Action { request =>
      if (request.accepts("application/hal+json"))
        try { f (request) } catch {
          case authErr: AuthException => Unauthorized(authErr.getMessage)
          case err: Exception => InternalServerError(err.getMessage)
        }
      else
        BadRequest("Expected acception of application/hal+json")
    }

  implicit val userWritesWrites  = Json.writes[User]
  implicit val gameSummaryWrites = Json.writes[GameSummary]
  implicit val gameStaticWrites  = Json.writes[GameStatic]
  implicit val gameDynamicWrites = Json.writes[GameDynamic]
}

