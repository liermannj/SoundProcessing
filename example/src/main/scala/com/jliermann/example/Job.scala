package com.jliermann.example

import akka.actor.ActorSystem
import akka.stream.scaladsl.Keep
import com.jliermann.example.domain.Category.Trash
import com.jliermann.example.environment.Environment

import javax.sound.sampled.TargetDataLine

object Job {

  def run(env: Environment, config: RootConfiguration, tdl: TargetDataLine)(implicit actorSystem: ActorSystem): Unit = {
    env.input.trainingFixture(env, config.featureConfiguration, tdl)
      .map {
        case (Trash, coefs) => s"TRASH;${coefs.mkString(";")}"
        case (category, coefs) => s"${category.label.toUpperCase};${coefs.mkString(";")}"
      }
      .toMat(env.output.sinkToFile(config.localConfiguration.outputFile))(Keep.right)
      .run()
  }

}
