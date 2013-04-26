import play.api._
import play.api.mvc._
import webapi._
import webapi.models._
import controllers._

trait MainApiModule extends ControllerCache {
  val gamesService: GamesService = new impl.GamesServiceMock
  val userAuth: UserAuth = new impl.UserBasicAuth(gamesService)
  val webapiController = registerController(new WebApi(userAuth, gamesService))
}

trait TestApiModule extends MainApiModule {
}

object MainApiModule extends MainApiModule
object TestApiModule extends TestApiModule

// MacwirePlayControllerWiring is responsible for controller creation
object Global extends PlayControllerWiring {

  // We have freedom in how we set up the application
  // - in this example we simply use a system property
  val module = if (System.getProperty("macwire-test-mode", "no") == "yes")
    TestApiModule
  else 
    MainApiModule
}
