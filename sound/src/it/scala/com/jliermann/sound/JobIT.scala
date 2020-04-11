package com.jliermann.sound

import com.jliermann.utils.test.{ResourcesTest, StreamTest}
import com.typesafe.config.ConfigFactory

import scala.concurrent.Await
import scala.concurrent.duration._

class JobIT extends StreamTest with ResourcesTest {

  "run" should "separate the word of the input" in {
    val rootConfig = RootConfiguration.loadConfigOrThrow(ConfigFactory.load())

    Await.result(Job.run(JobITEnvironment, rootConfig, null), 20.seconds)
    val result = readFile(rootConfig.localConfiguration.outputFile)

    result should have length 3
    rootConfig.localConfiguration.outputFile.delete()
  }
}
