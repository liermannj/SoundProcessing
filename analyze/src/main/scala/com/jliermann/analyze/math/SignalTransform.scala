package com.jliermann.analyze.math

import com.jliermann.analyze.domain.SignalTypes._

import scala.util.Try

trait SignalTransform {

  val signalTransform: SignalTransform.Service

}

object SignalTransform {

  trait Service {

    def fourier(env: SignalTransform, signal: Signal): Try[Fourier]

    def cosine(env: SignalTransform, signal: Signal): Try[Cosine]

    def fillSignal(signal: Signal): Try[Signal]

    def aggregateWindow(seq: Signal): Try[Coef]

    def normalize(seq: Signal): Try[Signal]
  }

}
