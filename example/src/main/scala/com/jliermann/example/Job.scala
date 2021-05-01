package com.jliermann.example

import akka.actor.ActorSystem
import akka.stream.scaladsl.{Keep, StreamConverters}
import akka.util.ByteString
import com.jliermann.example.domain.Category.Trash
import com.jliermann.example.environment.Environment

import javax.sound.sampled.TargetDataLine
import scala.io.StdIn

object Job {

  def run(env: Environment, config: RootConfiguration, tdl: TargetDataLine)(implicit actorSystem: ActorSystem): Unit = {
    val mode = StdIn.readLine("Choose mode ([R]: write features, [P]: predict) :")
    if(mode.toUpperCase == "R") write_features(env, config, tdl)
    else if(mode.toUpperCase == "P") predict(env, config, tdl)
    else println("You dickhead, it wasn't supposed to be difficult... stop.")
  }

  def write_features(env: Environment, config: RootConfiguration, tdl: TargetDataLine)(implicit actorSystem: ActorSystem): Unit = {
    env.input.trainingFixture(env, config.featureConfiguration, tdl)
      .map {
        case (Trash, coefs) => s"TRASH;${coefs.mkString(";")}"
        case (category, coefs) => s"${category.label.toUpperCase};${coefs.mkString(";")}"
      }
      .toMat(env.output.sinkToFile(config.localConfiguration.outputFile))(Keep.right)
      .run()
  }

  def predict(env: Environment, config: RootConfiguration, tdl: TargetDataLine)(implicit actorSystem: ActorSystem): Unit = {
    env.input.features(env, config.featureConfiguration, tdl)
      .via(env.predictor.predict(config.localConfiguration.predictor))
      .map(ByteString(_))
      .toMat(StreamConverters.fromOutputStream(() => System.out))(Keep.right)
      .run()
  }

  def fit_predict(env: Environment, config: RootConfiguration, tdl: TargetDataLine)(implicit actorSystem: ActorSystem): Unit = {
    env.input.trainingFixture(env, config.featureConfiguration, tdl)
      .map {
        case (Trash, coefs) => s"TRASH;${coefs.mkString(";")}"
        case (category, coefs) => s"${category.label.toUpperCase};${coefs.mkString(";")}"
      }
      .toMat(env.output.sinkToFile(config.localConfiguration.outputFile))(Keep.right)
      .run()
  }

}
