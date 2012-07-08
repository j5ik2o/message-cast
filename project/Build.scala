import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

  val appName = "message-cast"
  val appVersion = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    "org.sisioh" %% "scala-dddbase-core" % "0.0.1",
    "org.sisioh" %% "scala-dddbase-spec" % "0.0.1",
    "mysql" % "mysql-connector-java" % "5.1.18"
  )

  val main = PlayProject(appName, appVersion, appDependencies, mainLang = SCALA).settings(
    organization := "com.github.j5ik2o",
    resolvers ++= Seq(
      "Sisioh Maven Relase Repository" at "http://maven.sisioh.org/release/",
      "Sisioh Maven Snapshot Repository" at "http://maven.sisioh.org/snapshot/",
      "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
      "Twitter Repository" at "http://maven.twttr.com/"
    )

  )

}
