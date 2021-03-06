package com.jliermann.sound

import akka.actor.ActorSystem
import com.jliermann.sound.environment.EnvironmentLive
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.io.StdIn
import scala.util.{Failure, Success, Try}

private[sound] object Boot extends App with AudioApp with LazyLogging {
  implicit val actorSystem: ActorSystem = ActorSystem()
  val rootConfig = RootConfiguration.loadConfigOrThrow(ConfigFactory.load())
  for {
    tdl <- getTargetDataLine(rootConfig.soundConfiguration.audioFormat)
  } {
    Job.run(EnvironmentLive, rootConfig, tdl)
    logger.info("Speak into the microphone to start recording...")
    logger.info("Press enter to stop...")
    StdIn.readLine()
    logger.info("Wait for the flow to end...")
    Try(Await.result(actorSystem.terminate(), 10.second)) match {
      case Success(_) => logger.info("Flow terminated successfully")
      case Failure(exception) => throw exception
    }
  }

}
