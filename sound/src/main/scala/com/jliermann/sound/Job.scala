package com.jliermann.sound

import akka.actor.ActorSystem
import akka.stream.IOResult
import akka.stream.scaladsl.Keep
import com.jliermann.sound.environment.JobEnvironment
import javax.sound.sampled.TargetDataLine

import scala.concurrent.Future

private[sound] object Job {

  /**
   * Exemple d'utilisation :
   * 1. ingestion audio
   * 2. extraction selon les features prédéfinies
   * 3. Output fichier texte
   */
  def run(env: JobEnvironment, config: RootConfiguration, tdl: TargetDataLine)(implicit actorSystem: ActorSystem): Future[IOResult] = {
    env.wordSource.source(env, config.soundConfiguration, tdl)
      .via(env.featureExtraction.features(env, config.localConfiguration.fftFeatures))
      .map(xs => xs.mkString("[", "; ", "]"))
      .toMat(env.output.sinkToFile(config.localConfiguration.outputFile))(Keep.right)
      .run
  }
}
