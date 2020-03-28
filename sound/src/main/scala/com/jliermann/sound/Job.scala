package com.jliermann.sound

import akka.actor.ActorSystem
import com.jliermann.sound.domain.{Pitched, Silent}
import com.jliermann.sound.environment.JobEnvironment

private[sound] object Job {

  def run(env: JobEnvironment, config: RootConfiguration)(implicit actorSystem: ActorSystem): Unit = {

    println("GOOOOO")
   env.wordSource.source(env, config.soundConfiguration)
      .map(_.map {
        case Pitched(xs) => xs.mkString("[", "; ", "]")
        case Silent => "SILENT"
      })
      .map(_.mkString("{", "---", "}"))
      .runWith(env.output.sinkToFile(config.localConfiguration.outputFile))
  }
}
