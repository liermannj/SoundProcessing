package com.jliermann.analyze.math

import com.jliermann.analyze.domain.SignalTypes._

import scala.util.Try

trait FeatureExtractor {

  val featureExtractor: FeatureExtractor.Service

}

object FeatureExtractor {

  trait Service {
    def fourierCoefs(env: SignalTransform, fourier: Fourier, size: Int): Try[FourierCoefs]

    def mfc(env: SignalTransform, fourierCoefs: FourierCoefs, size: Int): Try[MFC]
  }
}
