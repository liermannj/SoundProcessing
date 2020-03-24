package com.jliermann.sound

import akka.actor.ActorSystem
import com.jliermann.sound.domain.{Pitched, Silent}
import com.jliermann.sound.environment.JobEnvironment
import com.jliermann.sound.output.OutputLive

private[sound] object Job {

  def run(env: JobEnvironment, config: RootConfiguration)(implicit actorSystem: ActorSystem): Unit = {

    println("GOOOOO")
   env.wordSource.source(env, config.soundConfiguration)
      .map(_.map {
        case Pitched(xs) => xs.mkString("[", "; ", "]")
        case Silent => "SILENT"
      })
      .map(_.mkString("{", "---", "}"))
      .runWith(OutputLive.sinkToFile(config.localConfiguration.outputFile))
  }
}
