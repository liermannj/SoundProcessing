package com.jliermann.analyze.domain

import org.apache.commons.math3.complex.Complex

object SignalTypes {

  type Enreg = Seq[Signal]
  type Signal = Seq[Double]
  type Hertz = Double
  type Mel = Double
  type Coef = Double
  type Coefs = Seq[Coef]

  final case class Fourier(xs: Seq[Complex])

  final case class Cosine(xs: Seq[Double])

  final case class FourierCoefs(coefs: Coefs)

  final case class MFC(coefs: Coefs)

}
