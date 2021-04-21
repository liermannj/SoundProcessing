package com.jliermann.analyze.math

import com.jliermann.analyze.domain.SignalTypes._
import com.jliermann.analyze.environment.{AggregateEnv, TransformatorEnv}
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
    val ceiled2Pow = fm.max(fm.pow(2, fm.ceil(fm.log(2, signal.length))).toInt, 2)
    val fillingSize = ceiled2Pow - signal.length
    signal ++ Seq.fill(fillingSize)(0D)
  }

  override def aggregateWindow(env: AggregateEnv)(seq: Signal): Try[Coef] = Try {
    val f = env.rawMath.triangular(seq.length)
    seq
      .zipWithIndex
      .map { case (value, index) => value * f(index) }
      .sum
  }

  override def normalize(seq: Signal): Signal = {
    if ((seq :+ 0D).distinct.length <= 1) seq
    else {
      val max = seq.map(fm.abs).max
      seq.map(_ / max)
    }
  }

  override def positiveShift(seq: Signal): Signal = {
    if ((seq :+ 0D).distinct.length <= 1) seq
    else {
      val min = fm.abs((seq :+ 0D).min)
      seq.map(_ + min)
    }
  }

}
