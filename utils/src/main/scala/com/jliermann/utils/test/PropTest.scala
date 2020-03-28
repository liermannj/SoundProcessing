package com.jliermann.utils.test

import org.scalatest.{FlatSpec, Matchers}
import org.scalatest.prop.GeneratorDrivenPropertyChecks

trait PropTest
  extends FlatSpec
    with GeneratorDrivenPropertyChecks
    with Matchers
    with PropGenUtilsTest