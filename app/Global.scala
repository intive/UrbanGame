import play.api._
import play.api.mvc._
import webapi._
import webapi.models._
import controllers._
import play.api.db.slick.DB

trait MainApiModule extends ControllerCache {
  lazy val slickDb: DB = DB
  lazy val gamesService: GamesService = new impl.GamesServiceSlick(slickDb)
  lazy val userAuth: UserAuth = new impl.UserBasicAuth(gamesService)
  val webapiController = registerController(new WebApi(userAuth, gamesService))
}

trait TestApiModule extends MainApiModule {
  override lazy val slickDb = DB("urbangameApi")
}

object MainApiModule extends MainApiModule
object TestApiModule extends TestApiModule

object Global extends PlayControllerWiring {
  import play.api.Play.current
  override lazy val module = 
    if (Play.isTest) TestApiModule
    else MainApiModule

  import play.api.i18n.Messages
  import play.api.libs.json.Json
  import play.api.mvc.Results._

  override def onHandlerNotFound(request: RequestHeader) =
    if (isWebApiRequest(request)) {
      val msg = Messages("webapi.error.resourceNotExists")
      val jsonMsg = Json.obj("code" -> 404, "message" -> msg)
      print (request.path)
      NotFound(Json.prettyPrint(jsonMsg)).as("application/json")
    }
    else {
      super.onHandlerNotFound(request)
    }
    
  override def onBadRequest(request: RequestHeader, msg: String) =
    if (isWebApiRequest(request)) {
      val jsonMsg = Json.obj("code" -> 400, "message" -> msg)
      BadRequest(Json.prettyPrint(jsonMsg)).as("application/json")
    }
    else {
      super.onBadRequest(request, msg)
    }

  def isWebApiRequest(request: RequestHeader) =
    request.path startsWith ("/api/")
}
