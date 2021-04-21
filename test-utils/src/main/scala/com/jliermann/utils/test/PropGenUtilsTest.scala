package com.jliermann.utils.test

import org.scalacheck.{Arbitrary, Gen, Shrink}

trait PropGenUtilsTest {
  implicit val arbDouble: Arbitrary[Double] = Arbitrary(Gen.choose(-1D, 1D))
  implicit val arbString: Arbitrary[String] = Arbitrary(Gen.alphaStr)

  implicit val noShrink: Shrink[String] = Shrink.shrinkAny
  implicit def noShrinkList[T]: Shrink[List[T]] = Shrink.shrinkAny

  implicit def arbSeq[T: Arbitrary]: Arbitrary[Seq[T]] = Arbitrary {
    for {
      n <- Gen.chooseNum(0, 1024)
      list <- Gen.listOfN(n, Arbitrary.arbitrary[T])
    } yield list
  }
}
