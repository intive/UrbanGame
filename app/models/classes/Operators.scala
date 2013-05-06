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
import models.mutils._

object Operators extends Table[OperatorsData]("OPERATORS") {
	def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
	def login = column[String]("login", O.NotNull)
	def pass_hash = column[String]("pass_hash", O.NotNull)
	def * = id.? ~ login ~ pass_hash <> (OperatorsData, OperatorsData.unapply _)
	def forInsert = login ~ pass_hash <> ({ t => 
      OperatorsData(None, t._1, t._2)}, 
      { (od: OperatorsData) => Some((od.login, od.pass_hash))
      })
}

trait Operators { this: ImplicitSession =>

	def createAccount(od: OperatorsData): Int = Operators.forInsert returning Operators.id insert od
	
}