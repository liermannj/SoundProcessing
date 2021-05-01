package com.jliermann.analyze.math

import com.jliermann.analyze.domain.SignalTypes._
import com.jliermann.analyze.environment._
import com.jliermann.analyze.seq.SeqOps._
import org.apache.commons.math3.util.{FastMath => fm}

import scala.util.{Success, Try}
import com.jliermann.analyze.seq.TrySeqOps._

object FeatureExtractorLive extends FeatureExtractorLive

trait FeatureExtractorLive extends FeatureExtractor.Service {

  import FeatureExtractorLive._

  override def fourierCoefs(env: FourierFeatureExtractorEnv, fourier: Fourier): Try[FourierCoefs] = {
    val absFourier = fourier.xs.map(_.abs)
    for {
      fundamental <- env.featureExtractor.fourierFundamental(fourier)
      coefs <- env.signalTransform.dicreteAggregate(env, fundamental)(absFourier)
    } yield FourierCoefs(fundamental, coefs)
  }

  override def mfc(env: MFCFeatureExtractorEnv, fourierCoefs: FourierCoefs): Try[MFC] = {
    for {
      melCoefs <- env.signalTransform.melScale(env, fourierCoefs)
      positiveLogCoefs = env.signalTransform.positiveShift(melCoefs)
      Cosine(xs) <- env.signalTransform.cosine(env, positiveLogCoefs)
      coefs <- env.signalTransform.dicreteAggregate(env, fourierCoefs.fundamental)(xs)
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