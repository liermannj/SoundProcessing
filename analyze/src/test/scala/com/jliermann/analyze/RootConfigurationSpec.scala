package com.jliermann.analyze

import com.typesafe.config.ConfigFactory
import org.scalatest.{FlatSpec, Matchers}

class RootConfigurationSpec extends FlatSpec with Matchers {

  "loadConfig" should "correctly load the configuration" in {
    RootConfiguration.loadConfigOrThrow(ConfigFactory.load("referenceSpec.conf").resolve())
    noException shouldBe thrownBy(RootConfiguration.loadConfigOrThrow(ConfigFactory.load("referenceSpec.conf").resolve()))
  }

}
