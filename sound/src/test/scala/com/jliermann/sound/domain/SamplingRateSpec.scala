package com.jliermann.sound.domain

import java.util.concurrent.TimeUnit

import com.jliermann.utils.test.PropSpec

class SamplingRateSpec extends PropSpec with SamplingRateGens {

  "ratePerSample" should "return the approximated time elapsed between two emissions" in forAll { sr: SamplingRate =>
    val result = sr.ratePerSample
    result.unit shouldBe TimeUnit.NANOSECONDS
    result.length.toDouble shouldBe <=(sr.timeUnit.length.toDouble / sr.samples)
  }

}
