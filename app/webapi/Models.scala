package webapi.models

import play.api._
import play.api.mvc._

case class User(id: Int)
case class GameSummary(gid: Int, name: String)
case class GameStatic (gid: Int, name: String)
case class GameDynamic(gid: Int, version: Int)

trait GamesService {
  def listGames(lat: Double, lon: Double, r: Double): List[GameSummary]
  def gameStatic(gid: Int): Option[GameStatic]
  def gameDynamic(gid: Int): Option[GameDynamic]
  def getUser(login: String, password: String): Option[User]
}

trait UserAuth {
  def authorize[A](request: Request[A]): User
  def apply[A](request: Request[A]): User = authorize(request)
}

class ApiException(code: Int, message: String) extends RuntimeException(message) {
  def getCode = code
}

class AuthException(msg: String) extends ApiException(401, msg)


