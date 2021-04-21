package com.jliermann.analyze.math

import com.jliermann.utils.test.PropTest
import org.scalacheck.Gen

class RawMathLiveSpec extends PropTest {

  "log" should "do a logaritmic function, and replace Nan values with 0" in forAll(Gen.posNum[Double]) { n: Double =>
    RawMathLive.log(n).isInfinite shouldBe false
    RawMathLive.log(n).isNaN shouldBe false
  }

}
