import play.api._
import play.api.mvc._
import webapi._
import webapi.models._
import controllers._

trait MainApiModule extends ControllerCache {
  lazy val gamesService: GamesService = ???
  lazy val userAuth: UserAuth = new impl.UserBasicAuth(gamesService)
  val webapiController = registerController(new WebApi(userAuth, gamesService))
}

trait TestApiModule extends MainApiModule {
  import play.api.db.slick.DB
  override lazy val gamesService: GamesService = new impl.GamesServiceSlick(DB)
}

object MainApiModule extends MainApiModule
object TestApiModule extends TestApiModule

object Global extends PlayControllerWiring {
  import play.api.Play.current

  override lazy val module = if (Play.isTest)
    TestApiModule
  else if (Play.isDev)
    TestApiModule
  else
    MainApiModule
}
