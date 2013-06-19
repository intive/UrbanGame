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

object Operators extends Table[Operator]("OPERATORS") {
  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def email = column[String]("email", O.NotNull)
  def password = column[String]("password", O.NotNull)
  def name = column[String]("name", O.NotNull)
  def logo = column[String]("logo", O.NotNull, O.Default("users/logo.png"))
  def description = column[Option[String]]("description")
  def permission = column[Permission]("permission", O.NotNull)
  def created = column[DateTime]("created", O.NotNull)
  def validated = column[Boolean]("validated", O.NotNull, O.Default(false))
  def token = column[Option[String]]("token")
  def * = id.? ~ email ~ password ~ name ~ logo ~ description ~ permission ~ 
    created ~ validated ~ token <> (Operator, Operator.unapply _)
  def forInsert = email ~ password ~ name ~ logo ~ description ~ permission ~ 
    created ~ validated ~ token <> ({ t => 
      Operator(None, t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9)}, 
      { (op: Operator) => Some((op.email, BCrypt.hashpw(op.password, BCrypt.gensalt()), 
        op.name, op.logo, op.description, op.permission, op.created, op.validated, op.token))
      })


  implicit val permissionTypeMapper = MappedTypeMapper.base[Permission, String](
    p => p match {
      case Administrator => "Administrator"
      case NormalUser => "NormalUser"
    },
    s => s match {
      case "Administrator" => Administrator
      case "NormalUser" => NormalUser
    }
  )
}

trait Operators { this: ImplicitSession =>
  import scala.util.{ Try, Success, Failure }

  def getRowsNo: Int = (for {op <- Operators} yield op.length).first

  def create(op: Operator): Try[Int] = Try(Operators.forInsert returning Operators.id insert op)

  def updateToken(email: String, token: Option[String]): Try[Int] = {
    val q = for {
      o <- Operators if o.email === email.bind
    } yield (o.validated ~ o.token)

    Try(q.update(if(token != None) false else true, token))

  }

  def authenticate(email: String, password: String): Option[Operator] = findByEmail(email).filter { operator => 
    operator.validated && BCrypt.checkpw(password, operator.password)
  }

  def findByEmail(email: String): Option[Operator] = Query(Operators).filter(_.email === email).firstOption

  def findById(id: Int): Option[Operator] = Query(Operators).filter(_.id === id).firstOption

  def findAll: Seq[Operator] = Query(Operators).list

  def findByCookie(cookie: String) = {
    val q = Query(Operators).filter{ s => play.api.libs.Crypto.sign(s.email.toString) == cookie }
  }
  
}