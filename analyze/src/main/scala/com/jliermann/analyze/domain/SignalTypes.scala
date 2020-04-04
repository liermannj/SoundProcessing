package com.jliermann.analyze.domain

import org.apache.commons.math3.complex.Complex
object SignalTypes {

  final case class Fourier(xs: Seq[Complex])

  final case class Cosine(xs: Seq[Double])

  final case class FourierCoefs(freq: Hertz, coefs: Coefs)

  final case class MFC(coefs: Coefs)

  type Signal = Seq[Double]

  type Hertz = Double

  type Coef = Double

  type Coefs = Seq[Coef]
}
