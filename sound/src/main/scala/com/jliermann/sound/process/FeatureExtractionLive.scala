package com.jliermann.sound.process

import akka.NotUsed
import akka.stream.scaladsl.Flow
import com.jliermann.sound.domain.{FFT, Pitched, Silent, SpokenTag}

import scala.util.{Failure, Success, Try}

object FeatureExtractionLive extends FeatureExtractionLive
trait FeatureExtractionLive extends FeatureExtraction.Service {

  override def intensity(frame: SpokenTag[Double]): Try[Double] = frame match {
    case Pitched(xs) => Try(xs.map(_.abs).max)
    case Silent => Success(0.0)
  }

  override def fft(fftFeatures: Int, frame: SpokenTag[Double]): Try[Seq[Double]] = frame match {
    case Pitched(xs) =>
      val powerOfTwo = (math.log10(xs.length)/math.log10(2)).floor
      val reducedToLog2 = xs.take(math.pow(2, powerOfTwo).toInt)
      Try(FFT.fft(reducedToLog2.toArray).toSeq.map(_.magnitude).take(fftFeatures))
    case Silent => Success(Seq.fill(fftFeatures)(0.0))
  }

  override def meansAndStds(features: Seq[Seq[Double]]): Try[Seq[Double]] = Try {
    features
      .transpose
      .flatMap { feature =>
        val mean = feature.sum / feature.length
        val std = feature.map(_ - mean).map(math.pow(_, 2)).sum / feature.length
        mean :: std :: Nil
      }
  }

  override def features(env: FeatureExtraction, fftFeatures: Int): Flow[Seq[SpokenTag[Double]], Seq[Double], NotUsed] = {
    Flow[Seq[SpokenTag[Double]]]
      .map { record =>
        val records = record
          .map(frame =>
            for {
              intensity <- env.featureExtraction.intensity(frame)
              fourier <- env.featureExtraction.fft(fftFeatures, frame)
            } yield intensity +: fourier
          )
          .flatMap(_.toOption)

        env.featureExtraction.meansAndStds(records)
      }
      .map {
        case Success(value) => value
        case Failure(_) => Seq.fill((fftFeatures + 1) * 2)(0.0) // fft + intensity * 2 (means and stds)
      }
  }
}