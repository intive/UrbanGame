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
import play.api.GlobalSettings

trait PlayControllerWiring extends GlobalSettings {
  lazy val module: ControllerCache = ???

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

