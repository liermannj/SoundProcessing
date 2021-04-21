package com.jliermann.sound.domain

import com.jliermann.utils.test.PropGenUtilsTest
import org.scalacheck.{Arbitrary, Gen}

object FrameGens

trait FrameGens extends PropGenUtilsTest {

  implicit def arbFrame: Arbitrary[Frame[Double]] = Arbitrary {
    Gen.listOf(Gen.choose(-1D, 1D)).map(xs => Frame(xs))
  }

}
