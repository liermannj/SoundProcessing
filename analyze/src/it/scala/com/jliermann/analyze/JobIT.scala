package com.jliermann.analyze

import java.io.File

import com.jliermann.analyze.environment.EnvironmentLive
import com.jliermann.utils.test.ResourcesTest
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.LazyLogging
import org.scalatest.{EitherValues, FlatSpec, Matchers}

class JobIT
  extends FlatSpec
    with Matchers
    with ResourcesTest
    with EitherValues
    with LazyLogging {
  val config: RootConfiguration = RootConfiguration.loadConfigOrThrow(ConfigFactory.load())

  "run" should "compute features for multiple enreg in a file" in withTmpFolder { tmpFolder =>
    val adaptedConfig = config.copy(
      localConfiguration = config.localConfiguration.copy(
        output = new File(tmpFolder, config.localConfiguration.output.getPath)))

    Job.run(EnvironmentLive, adaptedConfig) should be a 'success

    val result = readFile(adaptedConfig.localConfiguration.output)
    val expected = readResource(new File("expected.txt"))
    result should contain theSameElementsInOrderAs expected
  }

}
