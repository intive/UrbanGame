import play.api._
import play.api.mvc._
import webapi._
import webapi.models._
import controllers._

trait MainApiModule extends ControllerCache {
  import play.api.db.slick.DB
  lazy val gamesService: GamesService = new impl.GamesServiceSlick(DB)
  lazy val userAuth: UserAuth = new impl.UserBasicAuth(gamesService)
  val webapiController = registerController(new WebApi(userAuth, gamesService))
}

object MainApiModule extends MainApiModule

object Global extends PlayControllerWiring {
  import play.api.Play.current
  override lazy val module = MainApiModule
}
