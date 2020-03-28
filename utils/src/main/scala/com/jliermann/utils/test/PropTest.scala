package com.jliermann.utils.test

import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.scalatest.{FlatSpec, Matchers}

trait PropTest
  extends FlatSpec
    with GeneratorDrivenPropertyChecks
    with Matchers
    with PropGenUtilsTest