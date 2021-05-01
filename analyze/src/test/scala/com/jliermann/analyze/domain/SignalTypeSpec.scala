package com.jliermann.analyze.domain

import com.jliermann.analyze.domain.SignalTypes.{Cosine, Fourier, FourierCoefs}
import org.apache.commons.math3.complex.Complex
import org.scalacheck.{Arbitrary, Gen}

object SignalTypeSpec {

  implicit val arbFourier: Arbitrary[Fourier] = Arbitrary {
    for {
      real <- Arbitrary.arbitrary[Double]
      im <- Arbitrary.arbitrary[Double]
      fourier <- Gen.listOf(Gen.const(new Complex(real, im)))
    } yield Fourier(fourier)
  }

  implicit val arbCosine: Arbitrary[Cosine] = Arbitrary(Arbitrary.arbitrary[Seq[Double]].suchThat(_.length > 2).map(Cosine))

  implicit val arbFourierCoefs: Arbitrary[FourierCoefs] = Arbitrary {
    for {
      coefs <- Gen.nonEmptyListOf(Arbitrary.arbitrary[Double])
      fundamental <- Gen.choose(0, coefs.length-1)
    } yield FourierCoefs(fundamental, coefs)
  }

}
