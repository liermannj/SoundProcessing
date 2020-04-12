package com.jliermann.utils.test

import org.scalacheck.{Arbitrary, Gen, Shrink}

trait PropGenUtilsTest {
  implicit val arbDouble: Arbitrary[Double] = Arbitrary(Gen.choose(-1D, 1D))
  implicit val arbString: Arbitrary[String] = Arbitrary(Gen.alphaStr)

  implicit val noShrink: Shrink[String] = Shrink.shrinkAny

  implicit def noShrinkList[T]: Shrink[List[T]] = Shrink.shrinkAny
}
