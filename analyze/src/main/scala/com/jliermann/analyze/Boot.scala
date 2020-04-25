package com.jliermann.analyze

import com.jliermann.analyze.environment.EnvironmentLive
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.LazyLogging

import scala.util.{Failure, Success}

object Boot extends App with LazyLogging {
  val config = RootConfiguration.loadConfigOrThrow(ConfigFactory.load())

  import config.localConfiguration._

  Job.run(EnvironmentLive, config) match {
    case Success(_) =>
      logger.info(s"Successfully converted enregs from file ${input.file} to footPrints $output")
    case Failure(e) =>
      logger.error(e.toString, e.fillInStackTrace())
      throw new Exception("Some enregs were not successfully parsed")
  }
}
