package com.jliermann.sound

import akka.actor.ActorSystem
import com.jliermann.sound.environment.EnvironmentLive
import com.typesafe.config.ConfigFactory

private[sound] object Boot extends App {
    for {
      rootConfig <- RootConfiguration.loadConfig(ConfigFactory.load())
    } {
      implicit val actorSystem: ActorSystem = ActorSystem()
      Job.run(EnvironmentLive, rootConfig)
    }
}
