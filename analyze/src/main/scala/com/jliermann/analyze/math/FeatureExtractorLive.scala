package com.jliermann.analyze.math

import com.jliermann.analyze.FeatureExtractionConfig
import com.jliermann.analyze.domain.SignalTypes._
import com.jliermann.analyze.environment._
import org.apache.commons.math3.util.{FastMath => fm}

import scala.util.{Success, Try}

object FeatureExtractorLive extends FeatureExtractorLive

trait FeatureExtractorLive extends FeatureExtractor.Service {
  override def fourierCoefs(env: FourierFeatureExtractorEnv, fourier: Fourier, config: FeatureExtractionConfig): Try[FourierCoefs] = {
    for {
      fundamental <- env.featureExtractor.fourierFundamental(fourier)
      absFourier = fourier.xs.map(_.abs)

      coefs <- Try((fundamental until (config.size * fundamental) by fundamental)
        .map(middle => absFourier.slice(fm.max(0, middle - fundamental), fm.min(middle + fundamental, absFourier.length - 1)))
        .map(env.signalTransform.aggregateWindow)
        .map(_.getOrElse(0D)))

      normalizedOrSameCoefs <- config.normalize.filter(identity).fold[Try[Signal]](Success(coefs))(_ =>
        env.signalTransform.normalize(coefs))
    } yield FourierCoefs(fundamental, normalizedOrSameCoefs)
  }

  override def mfc(env: MFCFeatureExtractorEnv, fourierCoefs: FourierCoefs, config: FeatureExtractionConfig): Try[MFC] = {
    for {
      logCoefs <- Try(fourierCoefs.coefs.map(fm.log).map(n => if (n.isInfinity || n.isNaN) 0 else n))
      Cosine(xs) <- env.signalTransform.cosine(env, logCoefs.map(_ + fm.abs(logCoefs.min)))
      coefs = xs.tail.tail.take(config.size)
      normalizedOfSame <- config.normalize.filter(identity).fold[Try[Signal]](Success(coefs))(_ =>
        env.signalTransform.normalize(coefs))
    } yield MFC(normalizedOfSame)
  }

  def fourierFundamental(fourier: Fourier): Try[Int] = Try {
    val absFourier = fourier.xs.map(_.abs)
    val indexOfMax :: indexOfSecond :: _ = absFourier
      .zipWithIndex
      .sortBy(_._1)(Ordering[Double].reverse)
      .map(_._2)
      .toList

    fm.min(indexOfMax, indexOfSecond)
  }
}