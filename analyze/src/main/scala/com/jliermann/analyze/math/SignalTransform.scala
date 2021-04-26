package com.jliermann.analyze.math

import com.jliermann.analyze.domain.SignalTypes._
import com.jliermann.analyze.environment.{AggregateEnv, TransformatorEnv}

import scala.util.Try

trait SignalTransform {

  val signalTransform: SignalTransform.Service

}

object SignalTransform {

  trait Service {

    def fourier(env: TransformatorEnv, signal: Signal): Try[Fourier]

    def cosine(env: TransformatorEnv, signal: Signal): Try[Cosine]

    def melScale(env: RawMath, fourierCoefs: FourierCoefs): Try[Seq[Mel]]

    def fillSignal(signal: Signal): Try[Signal]

    def aggregateWindow(env: AggregateEnv)(seq: Signal): Try[Coef]

    def normalize(seq: Signal): Signal

    def positiveShift(seq: Signal): Signal
  }

}
