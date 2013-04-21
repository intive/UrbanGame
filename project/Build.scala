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
    "org.fusesource.scalate" % "scalate-core_2.10" % "1.6.1"
  )


  val main = play.Project(appName, appVersion, appDependencies)
  .settings(
    coffeescriptOptions := Seq("bare")
  )
  .settings(
    jasmineSettings : _*
  )
  .settings(
      // Jasmine configuration, overridden as we don't follow the default project structure sbt-jasmine expects
      appJsDir <+= baseDirectory { src => src / "target" / "scala-2.10" / "resource_managed" / "main" / "public" / "javascripts" },
      appJsLibDir <+= baseDirectory { src => src / "public" / "javascripts" / "lib" },
      jasmineTestDir <+= baseDirectory { src => src / "test" / "assets" },
      jasmineConfFile <+= baseDirectory { src => src / "test" / "assets" / "test.dependencies.js" },
      (test in Test) <<= (test in Test) dependsOn (jasmine)
    )
}
