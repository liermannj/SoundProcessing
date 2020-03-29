package com.jliermann.sound

import akka.actor.ActorSystem
import com.jliermann.sound.environment.EnvironmentLive
import com.typesafe.config.ConfigFactory

private[sound] object Boot extends App with AudioApp {
  val rootConfig = RootConfiguration.loadConfigOrThrow(ConfigFactory.load())
  for {
    tdl <- getTargetDataLine(rootConfig.soundConfiguration.audioFormat)
  } {
    implicit val actorSystem: ActorSystem = ActorSystem()
    Job.run(EnvironmentLive, rootConfig, tdl)
  }
}
