package com.jliermann.analyze.math

import com.jliermann.analyze.domain.SignalTypes._
import org.apache.commons.math3.util.{FastMath => fm}

import scala.util.Try

object FeatureExtractorLive extends FeatureExtractorLive

trait FeatureExtractorLive extends FeatureExtractor.Service {
  override def fourierCoefs(env: SignalTransform, fourier: Fourier, size: Int): Try[FourierCoefs] = Try {
    val absFourier = fourier.xs.map(_.abs)
    val indexOfMax :: indexOfSecond :: _ = absFourier
      .zipWithIndex
      .sortBy(_._1)(Ordering[Double].reverse)
      .map(_._2)
      .toList

    val firstIndex = fm.min(indexOfMax, indexOfSecond)

    val coefs = (firstIndex until (size * firstIndex) by firstIndex)
      .map(middle => absFourier.slice(fm.max(0, middle - firstIndex), fm.min(middle + firstIndex, absFourier.length - 1)))
      .map(env.signalTransform.aggregateWindow)
      .map(_.getOrElse(0D))

    env.signalTransform.normalize(coefs)
    .map(FourierCoefs(firstIndex.toDouble, _))
  }.flatten

  override def mfc(env: SignalTransform, fourierCoefs: FourierCoefs, size: Int): Try[MFC] = {
    for {
      logCoefs <- Try(fourierCoefs.coefs.map(fm.log).map(n => if (n.isInfinity || n.isNaN) 0 else n))
      Cosine(xs) <- env.signalTransform.cosine(env, logCoefs.map(_ + fm.abs(logCoefs.min)))
      normalized <- env.signalTransform.normalize(xs.tail.tail.take(size))
    } yield MFC(normalized)
  }
}