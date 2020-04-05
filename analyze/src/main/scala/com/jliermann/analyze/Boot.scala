package com.jliermann.analyze

import com.jliermann.analyze.environment.EnvironmentLive
import com.typesafe.config.ConfigFactory

object Boot extends App {
  val config = RootConfiguration.loadConfigOrThrow(ConfigFactory.load())
  Job.run(EnvironmentLive, config)
}
