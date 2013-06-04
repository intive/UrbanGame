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
    "jp.t2v" %% "play2.auth" % "0.9",
    "jp.t2v" %% "play2.auth.test" % "0.9" % "test"
  )


  val main = play.Project(appName, appVersion, appDependencies)
  .settings(
    resolvers += "julienrf.github.com" at "http://julienrf.github.com/repo/"
  )
  .settings(
    coffeescriptOptions := Seq("bare")
  )
  .settings(
    jasmineSettings : _*
  )
  .settings(
    // Jasmine configuration, overridden as we don't follow the default project structure sbt-jasmine expects
    appJsDir <+= baseDirectory( _ / "target" / "scala-2.10" / "resource_managed" / "main" / "public" / "javascripts"),
    appJsLibDir <+= baseDirectory( _ / "public" / "javascripts" / "lib"),
    jasmineTestDir <+= baseDirectory( _ / "test" / "assets"),
    jasmineConfFile <+= baseDirectory( _ / "test" / "assets" / "test.dependencies.js"),
    (test in Test) <<= (test in Test) dependsOn (jasmine)
  )
  .settings(
    scalacOptions ++= Seq("-deprecation","-unchecked","-feature")
  )
  .dependsOn(RootProject( uri("git://github.com/freekh/play-slick.git") ))
}
