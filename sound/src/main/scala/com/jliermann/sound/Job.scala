package com.jliermann.sound

import akka.actor.ActorSystem
import akka.stream.IOResult
import akka.stream.scaladsl.{Keep, RunnableGraph}
import com.jliermann.sound.domain.{Pitched, Silent}
import com.jliermann.sound.environment.JobEnvironment

import scala.concurrent.Future

private[sound] object Job {

  def run(env: JobEnvironment, config: RootConfiguration)(implicit actorSystem: ActorSystem): Future[IOResult] = {
    println("GOOOOO")
    env.wordSource.source(env, config.soundConfiguration)
      .map(_.map {
        case Pitched(xs) => xs.mkString("[", "; ", "]")
        case Silent => "SILENT"
      })
      .map(_.mkString("{", "---", "}"))
      .toMat(env.output.sinkToFile(config.localConfiguration.outputFile))(Keep.right)
      .run
    }
}
