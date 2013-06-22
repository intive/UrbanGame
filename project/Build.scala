import sbt._
import Keys._
import play.Project._
import com.gu.SbtJasminePlugin._

object ApplicationBuild extends Build {

  val appName         = "UrbanGame"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    jdbc,
    anorm,
    "org.fusesource.scalate" % "scalate-core_2.10" % "1.6.1",
    "commons-codec" % "commons-codec" % "1.6",
    "com.github.nscala-time" %% "nscala-time" % "0.4.0",
    "com.github.tototoshi" %% "slick-joda-mapper" % "0.2.1",
    "com.github.julienrf" %% "play-jsmessages" % "1.4.2",
    "org.mindrot" % "jbcrypt" % "0.3m",
    "jp.t2v" %% "play2.auth" % "0.9",
    "jp.t2v" %% "play2.auth.test" % "0.9" % "test",
    "com.typesafe" %% "play-plugins-mailer" % "2.1.0",
    "net.tanesha.recaptcha4j" % "recaptcha4j" % "0.0.7"
  )


  val main = play.Project(appName, appVersion, appDependencies)
  .settings(
    resolvers += "julienrf.github.com" at "http://julienrf.github.com/repo/"
  )
  .settings(
    resolvers += "jbcrypt repo" at "http://mvnrepository.com/"
  )
  .settings(
    coffeescriptOptions := Seq("bare")
  )
  .settings(
    scalacOptions ++= Seq("-deprecation","-unchecked","-feature")
  )
  .settings(
    sbt.Keys.fork in Test := false
  )
  .dependsOn(RootProject( uri("git://github.com/freekh/play-slick.git") ))
}
