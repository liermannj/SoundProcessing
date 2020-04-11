import sbt.Keys._
import sbt._

val AkkaStreamVersion = "2.6.3"
val PureConfigVersion = "0.12.3"
val ScalaTestVersion = "3.0.1"
val ScalaCheckVersion = "1.14.3"
val ScalaArmVersion = "2.0"
val LogbackVersion = "3.9.2"
val LogbackBackend = "1.2.3"

lazy val commonSettings = Seq(name := "SoundProcessing"
  , version := "0.1-SNAPSHOT")

lazy val root = project.in(file("."))
  .settings(commonSettings: _*)
  .aggregate(sound)

lazy val utils = project.in(file("utils"))
  .settings(commonSettings: _*)
  .settings(libraryDependencies += "com.typesafe.akka" %% "akka-stream" % AkkaStreamVersion
    , libraryDependencies += "org.scalatest" %% "scalatest" % ScalaTestVersion
    , libraryDependencies += "org.scalacheck" %% "scalacheck" % ScalaCheckVersion
    , libraryDependencies += "com.jsuereth" %% "scala-arm" % ScalaArmVersion)

lazy val sound = project.in(file("sound"))
  .dependsOn(utils)
  .configs(IntegrationTest)
  .settings(Defaults.itSettings)
  .settings(commonSettings: _*)
  .settings(Compile / mainClass := Some("com.jliermann.sound.Boot"))
  .settings(libraryDependencies += "com.github.pureconfig" %% "pureconfig" % PureConfigVersion
    , libraryDependencies += "ch.qos.logback" % "logback-classic" % LogbackBackend
    , libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % LogbackVersion)
