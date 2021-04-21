package com.jliermann.sound.domain

import com.jliermann.utils.test.PropGenUtilsTest
import org.scalacheck.{Arbitrary, Gen}

object FrameGens
trait FrameGens extends PropGenUtilsTest {

  implicit def arbFrame: Arbitrary[Frame[Double]] = Arbitrary {
    for {
      list <- Arbitrary.arbitrary[Seq[Double]]
    } yield Frame(list)
  }

  implicit def arbPitched: Arbitrary[Pitched[Double]] = Arbitrary {
    for {
      list <- Arbitrary.arbitrary[Seq[Double]]
    } yield Pitched(list)
  }

  implicit def arbSpokenTag: Arbitrary[SpokenTag[Double]] = Arbitrary {
    Gen.oneOf(Arbitrary.arbitrary[Pitched[Double]], Gen.const(Silent))
  }

}
