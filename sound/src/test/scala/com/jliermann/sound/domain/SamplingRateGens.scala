package com.jliermann.sound.domain

import com.jliermann.utils.test.PropGenUtilsTest
import org.scalacheck.{Arbitrary, Gen}

import scala.concurrent.duration.FiniteDuration

object SamplingRateGens extends SamplingRateGens

trait SamplingRateGens extends PropGenUtilsTest {

  implicit val arbSamplingRate: Arbitrary[SamplingRate] = Arbitrary {
    for {
      sample: Long <- Gen.choose(1, Long.MaxValue)
      unit: FiniteDuration <- Gen.finiteDuration.map(d => FiniteDuration(math.abs(d.length), d.unit))
    } yield SamplingRate(sample, unit)
  }

}
