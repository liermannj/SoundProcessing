import sbt._
import Keys._

val AkkaStreamVersion = "2.6.3"
val ScalaTestVersion = "3.0.1"
val ScalaCheckVersion = "1.14.3"

lazy val commonSettings = Seq(name := "SoundProcessing",
  version := "0.1-SNAPSHOT")

lazy val root = project.in(file("."))
  .settings(commonSettings: _*)
  .aggregate(sound)

lazy val utils = project.in(file("utils"))
  .settings(commonSettings:_*)
  .settings(libraryDependencies += "com.typesafe.akka" %% "akka-stream" % AkkaStreamVersion
    , libraryDependencies += "org.scalatest" %% "scalatest" % ScalaTestVersion
    , libraryDependencies += "org.scalacheck" %% "scalacheck" % ScalaCheckVersion)

