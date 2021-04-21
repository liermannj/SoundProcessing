package com.jliermann.sound.process

import akka.NotUsed
import akka.stream.scaladsl.Flow
import com.jliermann.sound.domain.SpokenTag

import scala.util.Try

trait FeatureExtraction {

  val featureExtraction: FeatureExtraction.Service

}

object FeatureExtraction {

  trait Service {

    def intensity(frame: SpokenTag[Double]): Try[Double]

    def fft(fftFeatures: Int, frame: SpokenTag[Double]): Try[Seq[Double]]

    def meansAndStds(features: Seq[Seq[Double]]): Try[Seq[Double]]

    def features(env: FeatureExtraction, fftFeatures: Int): Flow[Seq[SpokenTag[Double]], Seq[Double], NotUsed]

  }

}
