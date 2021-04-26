package com.jliermann.analyze.math

import com.jliermann.analyze.FeatureExtractionConfig
import com.jliermann.analyze.domain.SignalTypes._
import com.jliermann.analyze.environment._
import com.jliermann.analyze.seq.SeqOps._
import org.apache.commons.math3.util.{FastMath => fm}

import scala.util.Try

object FeatureExtractorLive extends FeatureExtractorLive {
  val IgnoredMFCCoefs = 2
}

trait FeatureExtractorLive extends FeatureExtractor.Service {

  import FeatureExtractorLive._

  override def fourierCoefs(env: FourierFeatureExtractorEnv, fourier: Fourier, config: FeatureExtractionConfig): Try[FourierCoefs] = {
    val absFourier = fourier.xs.map(_.abs)
    for {
      fundamental <- env.featureExtractor.fourierFundamental(fourier)
      coefs <- Try((fundamental until (config.size * fundamental) by fundamental) // harmonics are multiple of fundamental
        .map(middle => absFourier.safeCenteredSlice(middle, fundamental)) // overlapping window around harmonics, to not miss it in case of approximation
        .map(env.signalTransform.aggregateWindow(env))
        .map(_.getOrElse(0D)))

    } yield FourierCoefs(fundamental, coefs)
  }

  override def mfc(env: MFCFeatureExtractorEnv, fourierCoefs: FourierCoefs, config: FeatureExtractionConfig): Try[MFC] = {
    for {
      melCoefs <- env.signalTransform.melScale(env, fourierCoefs)
      positiveLogCoefs = env.signalTransform.positiveShift(melCoefs)
      Cosine(xs) <- env.signalTransform.cosine(env, positiveLogCoefs)
      coefs = xs.slice(IgnoredMFCCoefs, config.size + IgnoredMFCCoefs)

    } yield MFC(coefs)
  }

  /**
   * @param fourier complex fourier sequence
   * @return smallest index between the two first pics for consolidation
   */
  override def fourierFundamental(fourier: Fourier): Try[Int] = Try {
    val absFourier = fourier.xs.map(_.abs)
    val indexOfMax :: indexOfSecond :: _ = absFourier
      .zipWithIndex
      .sortBy(_._1)(Ordering[Double].reverse)
      .map(_._2)
      .toList

    fm.min(indexOfMax, indexOfSecond)
  }
}