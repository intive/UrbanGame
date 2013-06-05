/**Copyright 2013 BLStream, BLStream's Patronage Program Contributors
 *     http://blstream.github.com/UrbanGame/
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package models

import play.api.db.slick.Config.driver.simple._
import com.github.tototoshi.slick.JodaSupport._
import com.github.nscala_time.time.Imports._

case class User(id: Option[Int], login: String, hash: String)

object Users extends Table[User]("USERS") {
  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def login  = column[String]("login", O.NotNull)
  def hash   = column[String]("hash", O.NotNull)
  def * = id.? ~ login ~ hash <> (User, User.unapply _)
}

