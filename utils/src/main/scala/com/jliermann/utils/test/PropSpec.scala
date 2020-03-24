package com.jliermann.utils.test

import org.scalatest.{FlatSpec, Matchers}
import org.scalatest.prop.GeneratorDrivenPropertyChecks

trait PropSpec
  extends FlatSpec
    with GeneratorDrivenPropertyChecks
    with Matchers
    with PropGenUtilsSpec