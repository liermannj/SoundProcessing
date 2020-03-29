package com.jliermann.sound

import com.typesafe.config.ConfigFactory
import org.scalatest.{FlatSpec, Matchers, TryValues}

class RootConfigurationSpec extends FlatSpec with Matchers with TryValues {

  "loadConfig" should "correctly load the configuration" in {
    noException shouldBe thrownBy(RootConfiguration.loadConfigOrThrow(ConfigFactory.load("referenceSpec.conf").resolve()))
  }

}
