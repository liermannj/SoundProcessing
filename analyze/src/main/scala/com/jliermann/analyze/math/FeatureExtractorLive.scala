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
      coefs <- Try((fundamental until (config.size * fundamental) by fundamental)
        .map(middle => absFourier.safeCenteredSlice(middle, fundamental))
        .map(env.signalTransform.aggregateWindow(env))
        .map(_.getOrElse(0D)))

    } yield config.normalize
      .filter(identity)
      .fold(FourierCoefs(fundamental, coefs))(_ => FourierCoefs(fundamental, env.signalTransform.normalize(coefs)))
  }

  override def mfc(env: MFCFeatureExtractorEnv, fourierCoefs: FourierCoefs, config: FeatureExtractionConfig): Try[MFC] = {
    for {
      logCoefs <- Try(fourierCoefs.coefs.map(env.rawMath.log))
      positiveLogCoefs = env.signalTransform.positiveShift(logCoefs)
      Cosine(xs) <- env.signalTransform.cosine(env, positiveLogCoefs)
      coefs = xs.slice(IgnoredMFCCoefs, config.size + IgnoredMFCCoefs)

    } yield config.normalize
      .filter(identity)
      .fold(MFC(coefs))(_ => MFC(env.signalTransform.normalize(coefs)))
  }

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