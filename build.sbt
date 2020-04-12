import sbt.Keys._
import sbt._

val AkkaStreamVersion = "2.6.3"
val PureConfigVersion = "0.12.3"
val ScalaTestVersion = "3.0.1"
val ScalaCheckVersion = "1.14.3"
val ScalaArmVersion = "2.0"
val LogbackVersion = "3.9.2"
val LogbackBackend = "1.2.3"
val ApacheCommonsVersion = "3.0"

val commonSettings = Seq(name := "SoundProcessing"
  , version := "0.1-SNAPSHOT")

def anyProject(n: String): Project = {
  Project(n, file(n))
    .settings(commonSettings: _*)
}

def runnableProject(n: String, boot: String) = {
  anyProject(n)
    .settings(Compile / mainClass := Some(boot))
    .configs(IntegrationTest)
    .settings(Defaults.itSettings)
}

lazy val root = project.in(file("."))
  .settings(commonSettings: _*)
  .aggregate(
    sound,
    analyze)

lazy val testUtils = anyProject("test-utils")
  .settings(libraryDependencies += "com.typesafe.akka" %% "akka-stream" % AkkaStreamVersion
    , libraryDependencies += "org.scalatest" %% "scalatest" % ScalaTestVersion
    , libraryDependencies += "org.scalacheck" %% "scalacheck" % ScalaCheckVersion
    , libraryDependencies += "com.jsuereth" %% "scala-arm" % ScalaArmVersion)

lazy val graphUtils = anyProject("graph-utils")
  .dependsOn(testUtils % Test)
  .settings(libraryDependencies += "com.typesafe.akka" %% "akka-stream" % AkkaStreamVersion)

lazy val sound = runnableProject("sound", "com.jliermann.sound.Boot")
  .dependsOn(testUtils % IntegrationTest)
  .dependsOn(testUtils % Test)
  .dependsOn(graphUtils)
  .settings(libraryDependencies += "com.github.pureconfig" %% "pureconfig" % PureConfigVersion
    , libraryDependencies += "com.jsuereth" %% "scala-arm" % ScalaArmVersion
    , libraryDependencies += "ch.qos.logback" % "logback-classic" % LogbackBackend
    , libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % LogbackVersion)

lazy val analyze = runnableProject("analyze", "com.jliermann.analyze.Boot")
  .dependsOn(testUtils % IntegrationTest)
  .dependsOn(testUtils % Test)
  .settings(libraryDependencies += "com.github.pureconfig" %% "pureconfig" % PureConfigVersion
    , libraryDependencies += "org.apache.commons" % "commons-math3" % ApacheCommonsVersion
    , libraryDependencies += "com.jsuereth" %% "scala-arm" % ScalaArmVersion
    , libraryDependencies += "ch.qos.logback" % "logback-classic" % LogbackBackend
    , libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % LogbackVersion)