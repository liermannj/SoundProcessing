package com.jliermann.sound.domain

import org.scalacheck.{Arbitrary, Gen}

case class MatrixGen(xxs: Seq[Seq[Double]])

object MatrixGen {
  implicit val implMatrix: Arbitrary[MatrixGen] = Arbitrary {
    for {
      x <- Gen.choose(1, 20)
      y <- Gen.choose(1, 20)
    } yield MatrixGen(Seq.fill(x)(Seq.fill(y)(math.random()*2-1)))
  }
}
