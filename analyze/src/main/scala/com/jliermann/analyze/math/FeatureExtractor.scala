package com.jliermann.analyze.math

import com.jliermann.analyze.domain.SignalTypes._
import com.jliermann.analyze.environment._

import scala.util.Try

trait FeatureExtractor {

  val featureExtractor: FeatureExtractor.Service

}

object FeatureExtractor {

  trait Service {
    def fourierCoefs(env: FourierFeatureExtractorEnv, fourier: Fourier): Try[FourierCoefs]

    def mfc(env: MFCFeatureExtractorEnv, fourierCoefs: FourierCoefs): Try[MFC]

    def fourierFundamental(fourier: Fourier): Try[Int]
  }

}
