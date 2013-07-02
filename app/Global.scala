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
  override lazy val module = TestApiModule
}
