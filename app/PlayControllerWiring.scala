import play.api.GlobalSettings

trait PlayControllerWiring extends GlobalSettings {

  val module: ControllerCache

  // takes controller from cache or resorts to defaults
  override def getControllerInstance[T](clazz: Class[T]) =
    module.controllers(clazz).map(_.asInstanceOf[T]).getOrElse(
      super.getControllerInstance(clazz))
}

trait ControllerCache {
  import scala.collection.mutable.Map

  private lazy val wireCache = Map[Class[_], Any]()

  def registerController[T](obj: T) = {
    wireCache.put(obj.getClass, obj)
    obj
  }

  val controllers = wireCache.lift
}

