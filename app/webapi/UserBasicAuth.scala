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
package webapi.impl

import play.api._
import play.api.mvc._
import webapi.models._

class UserBasicAuth(gamesService: GamesService) extends UserAuth {
  override def authorize[A](request: Request[A]): User = {
    val header = getAuthHeader(request)
    val (login, hash) = parseHeader(header)
    gamesService.getUserOpt(login, hash) getOrElse { 
      throw new AuthException(preMsg+"invalidUserOrPassword")
    }
  }
 
  import org.apache.commons.codec.binary.Base64
  import scala.util.matching.Regex

  private def getAuthHeader[A](request: Request[A]): String =
    request.headers.get("Authorization") getOrElse { 
      throw new AuthException(preMsg+"noAuthHeader")
    }

  private def decode(encoded: String):Option[String] = {
    def isLegalChar(c: Char) = (c >= ' ' && c <= '~')
    val res = new String(Base64.decodeBase64(encoded))
    if (res forall isLegalChar) Some(res) else None
  } 
  
  private def parseHeader(header: String): (String,String) = {
    val pattern = new Regex("""Basic ([\w\d\+/=]+)""", "base64")
    val res = for {
      matched <- pattern findFirstMatchIn header
      decoded <- decode(matched group ("base64"))
      (login, hash) <- Some(decoded span {_ != ':'})
    } yield (login, hash drop 1)

    res getOrElse { throw new AuthException(preMsg+"invalidAuthHeader") }
  }

  val preMsg = "webapi.error."
}
