package webapi.impl

import play.api._
import play.api.mvc._
import webapi.models._

class GamesServiceMock extends GamesService {
  override def listGames(lat: Double, lon: Double, r: Double) = list
  override def gameStatic(gid: Int) = static get gid
  override def gameDynamic(gid: Int) = dynamic get gid
  override def getUser(user: String, hash: String) = users get user    
  
  private val list = List(
    GameSummary(123, "dasdsd"),
    GameSummary(32, "dasdsd"),
    GameSummary(564, "dasdsd")
  )

  private val static = Map(
    123 -> GameStatic(123, "dasdsd"),
    32  -> GameStatic(32, "dasdsd"),
    564 -> GameStatic(564, "dasdsd")
  )

  private val dynamic = Map(
    123 -> GameDynamic(123, 0),
    32  -> GameDynamic( 32, 1),
    564 -> GameDynamic(564, 2)
  )

  private val users = Map(
    "admin" -> User(0),
    "user" -> User(666)
  )
}

class UserBasicAuth(gamesService: GamesService) extends UserAuth {
  
  override def authorize[A](request: Request[A]): User = {
    val header = getAuthHeader(request)
    val (login, hash) = parseHeader(header)
    gamesService.getUser(login, hash) getOrElse { 
      throw new AuthException("User not found")
    }
  }
 
  import org.apache.commons.codec.binary.Base64
  import scala.util.matching.Regex

  private def getAuthHeader[A](request: Request[A]): String =
    request.headers.get("Authorization") getOrElse { 
      throw new AuthException("No Auth header!")
    }

  private def decode(encoded: String):Option[String] = {
    def isLegalChar(c: Char) = (c >= ' ' && c <= '~')
    val res = new String(Base64.decodeBase64(encoded))
    if (res forall isLegalChar) Some(res) else None
  } 
  
  private def parseHeader(header: String): (String,String) = {
    val pattern = new Regex("""(?i)\QBasic\E +([\w\d\+/=]+)""", "base64")
    val res = for {
      matched <- pattern findFirstMatchIn header
      decoded <- decode(matched group ("base64"))
      (login, hash) <- Some(decoded span {_ != ':'})
    } yield (login, hash drop 1)

    res getOrElse { throw new AuthException("Invalid Auth header!") }
  }
}
