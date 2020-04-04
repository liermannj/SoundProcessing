package com.jliermann.sound

import com.jliermann.sound.Boot.getTargetDataLine
import com.jliermann.utils.test.{ResourcesTest, StreamTest}
import com.typesafe.config.ConfigFactory

import scala.concurrent.Await
import scala.concurrent.duration._

class JobIT extends StreamTest with ResourcesTest {

  "run" should "separate the word of the input" in {
    val rootConfig = RootConfiguration.loadConfigOrThrow(ConfigFactory.load())
    for {
      tdl <- getTargetDataLine(rootConfig.soundConfiguration.audioFormat)
    } {
      Await.result(Job.run(JobITEnvironment, rootConfig, tdl), 20.seconds)
      val result = readFile(rootConfig.localConfiguration.outputFile)
      result should have length 3
      rootConfig.localConfiguration.outputFile.delete()
    }
  }

}
