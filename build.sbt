import sbt.Keys._
import sbt._

val AkkaStreamVersion = "2.6.3"
val PureConfigVersion = "0.12.3"
val ScalaTestVersion = "3.0.1"
val ScalaCheckVersion = "1.14.3"
val ScalaArmVersion = "2.0"
val CatsVersion = "2.0.0"

lazy val commonSettings = Seq(name := "SoundProcessing",
  version := "0.1-SNAPSHOT")

lazy val root = project.in(file("."))
  .settings(commonSettings: _*)
  .aggregate(sound)

lazy val utils = project.in(file("utils"))
  .settings(commonSettings:_*)
  .settings(libraryDependencies += "com.typesafe.akka" %% "akka-stream" % AkkaStreamVersion
    , libraryDependencies += "org.scalatest" %% "scalatest" % ScalaTestVersion
    , libraryDependencies += "org.scalacheck" %% "scalacheck" % ScalaCheckVersion
    , libraryDependencies += "com.jsuereth" %% "scala-arm" % ScalaArmVersion)

lazy val sound = project.in(file("sound"))
  .dependsOn(utils)
  .settings(commonSettings: _*)
  .settings(Compile / mainClass := Some("com.jliermann.sound.Boot"))
  .settings(libraryDependencies += "com.typesafe.akka" %% "akka-stream" % AkkaStreamVersion
    , libraryDependencies += "com.github.pureconfig" %% "pureconfig" % PureConfigVersion)
  .settings(libraryDependencies += "com.typesafe.akka" %% "akka-stream-testkit" % AkkaStreamVersion % Test
    , libraryDependencies += "org.scalatest" %% "scalatest" % ScalaTestVersion % Test
    , libraryDependencies += "org.scalacheck" %% "scalacheck" % ScalaCheckVersion % Test)
