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
package controllers

import play.api.mvc.{Action, Controller}
import play.api.i18n.Lang
import play.api.data._
import play.api.data.Forms._
import play.api.Logger
import play.api.Play.current

trait CookieLang extends Controller {

  val langform = Form("lang" -> nonEmptyText)

  val changeLang = Action { implicit request =>
    val referrer = request.headers.get(REFERER).getOrElse(HOME_URL)
    langform.bindFromRequest.fold(
      errors => {
        Logger.logger.debug("The locale can not be change to : " + errors)
        BadRequest(referrer)
      },
      lang => {
        Logger.logger.debug("Change user lang to : " + lang)
        Redirect(referrer).withLang(Lang(lang))
      }
    )
  }

  protected val HOME_URL = "/"
}