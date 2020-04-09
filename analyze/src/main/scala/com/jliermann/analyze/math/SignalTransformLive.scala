package com.jliermann.analyze.math

import com.jliermann.analyze.domain.SignalTypes._
import com.jliermann.analyze.environment.TransformatorEnv
import org.apache.commons.math3.transform._
import org.apache.commons.math3.util.{FastMath => fm}

import scala.util.Try

object SignalTransformLive extends SignalTransformLive

trait SignalTransformLive extends SignalTransform.Service {

  private val fourierTransformer = new FastFourierTransformer(DftNormalization.STANDARD)
  private val cosineTransformer = new FastCosineTransformer(DctNormalization.STANDARD_DCT_I)

  override def fourier(env: TransformatorEnv, signal: Signal): Try[Fourier] = {
    for {
      filledSignal <- env.signalTransform.fillSignal(signal)
      transformed <- Try(fourierTransformer.transform(filledSignal.toArray, TransformType.FORWARD))
    } yield Fourier(transformed)
  }

  override def cosine(env: TransformatorEnv, signal: Signal): Try[Cosine] = {
    for {
      filledSignal <- env.signalTransform.fillSignal(signal)
      transformed <- Try(cosineTransformer.transform((filledSignal :+ 0D).toArray, TransformType.FORWARD))
    } yield Cosine(transformed)
  }

  override def fillSignal(signal: Signal): Try[Signal] = Try {
    val ceiled2Pow = fm.pow(2, fm.ceil(fm.log(2, signal.length))).toInt
    val fillingSize = ceiled2Pow - signal.length
    signal ++ Seq.fill(fillingSize)(0D)
  }

  override def aggregateWindow(seq: Signal): Try[Coef] = Try {
    // triangular signal
    val f = (x: Int) => -fm.abs(-1 + 2 * (x.toDouble / seq.length)) + 1
    seq
      .zipWithIndex
      .map { case (value, index) => value * f(index) }
      .sum
  }

  override def normalize(seq: Signal): Try[Signal] = Try {
    seq.map(_ / seq.map(fm.abs).max)
  }


}
