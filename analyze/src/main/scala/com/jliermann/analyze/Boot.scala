package com.jliermann.analyze

import com.jliermann.analyze.environment.EnvironmentLive
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.LazyLogging

object Boot extends App with LazyLogging {
  val config = RootConfiguration.loadConfigOrThrow(ConfigFactory.load())

  import config.localConfiguration._

  Job.run(EnvironmentLive, config) match {
    case Right(_) =>
      logger.info(s"Successfully converted enregs from file ${input.file} to footPrints $output")
    case Left(exceptions) =>
      exceptions.foreach(e => logger.error(s"Something went wrong : ", e))
      throw new Exception("Some enregs were not successfully parsed")

  }
}
