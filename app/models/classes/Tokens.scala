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

import play.api.Play.current
import play.api.db.slick.DB
import play.api.db.slick.Config.driver.simple._
import com.github.tototoshi.slick.JodaSupport._
import com.github.nscala_time.time.Imports._
import org.mindrot.jbcrypt.BCrypt
import models.utils._

object Tokens extends Table[Token]("TOKENS") {
  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def opId = column[Int]("opId", O.NotNull)
  def token = column[String]("token", O.NotNull)
  def series = column[String]("series", O.NotNull)
  def created = column[DateTime]("created", O.NotNull)
  def expires = column[DateTime]("expires", O.NotNull)
  def rememberme = column[Boolean]("rememberme", O.NotNull)
  def * = id.? ~ opId ~ token ~ series ~ created ~ expires ~ rememberme <> (Token, Token.unapply _)
  def forInsert = opId ~ token ~ series ~ created ~ expires ~ rememberme <> ({ t => 
      Token(None, t._1, t._2, t._3, t._4, t._5, t._6)}, 
      { (tn: Token) => Some((tn.opId, tn.token, tn.series, tn.created, tn.expires, tn.rememberme))
      })
}

trait Tokens { this: ImplicitSession =>
  import scala.util.{ Try, Success, Failure }

  def create(tn: Token): Try[Int] = Try(Tokens.forInsert returning Tokens.id insert tn)

  def delete(tid: Int, rememberme: Boolean): Try[Int] = {
    val q = for {
      t <- Tokens if t.id === tid.bind && t.rememberme === rememberme.bind
    } yield t

    Try(q.delete)
  }

  def findById(id: Int): Option[Token] = Query(Tokens).filter(_.id === id).firstOption

  def findByKeys(opId: Int, token: String, series: String): Option[Token] = Query(Tokens).filter{ s => 
    s.opId === opId && s.token === token && s.series === series 
  }.firstOption
  
}