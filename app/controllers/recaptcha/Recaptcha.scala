package controllers

import play.api._
import play.api.mvc._
import play.api.Play
import play.api.Play.current
import net.tanesha.recaptcha.{ReCaptchaFactory, ReCaptchaImpl, ReCaptchaResponse}

object Recaptcha extends Recaptcha

trait Recaptcha {

  val recaptchaUrl = "http://www.google.com/recaptcha/api/verify"
  val privateKey = Play.current.configuration.getString("captcha.privateKey").get
  val publicKey = Play.current.configuration.getString("captcha.publicKey").get

  def validate(chal: String, resp: String)(implicit request: RequestHeader): Boolean = {
    val addr = request.remoteAddress
    val reCaptcha = new ReCaptchaImpl()
    reCaptcha.setPrivateKey(privateKey)
    val reCaptchaResponse = reCaptcha.checkAnswer(addr, chal, resp)
    reCaptchaResponse.isValid()
  }
}