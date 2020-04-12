package com.jliermann.analyze.preprocess

import com.jliermann.analyze.AnalyzeConfiguration
import com.jliermann.analyze.domain.Matrix
import com.jliermann.analyze.domain.SignalTypes.{Coefs, FourierCoefs, MFC, Signal}
import com.jliermann.analyze.environment.{EnregPreparatorEnv, PreparatorEnv}
import com.jliermann.analyze.seq.NumericSeqOps._
import com.jliermann.analyze.seq.TrySeqOps._

import scala.util.{Failure, Success, Try}

object PreparatorLive extends PreparatorLive {

  case class ImproperEnregException(enreg: Seq[Signal]) extends ArithmeticException(s"unable to compute footprint for enreg $enreg")

  case object EmptyEnregException extends IllegalArgumentException("Can't compute features for empty enreg")

}

trait PreparatorLive extends Preparator.Service {

  import PreparatorLive._

  override def prepareEnreg(env: EnregPreparatorEnv, config: AnalyzeConfiguration)(enreg: Seq[Signal]): Either[Seq[Throwable], Coefs] = {
    enreg.map(env.preparator.prepareSignal(env, config)) match {
      case Nil => Left(EmptyEnregException :: Nil)
      case unitProcessed if unitProcessed.exists(_.isFailure) => Left(ImproperEnregException(enreg) +: unitProcessed.failures)
      case unitProcessed =>
        val coefsT = for {
          matrix <- Matrix(unitProcessed.successes)
          coefs <- Try(matrix.transpose.xxs.flatMap(xs => xs.mean :: xs.stdDev :: Nil))
        } yield coefs

        coefsT match {
          case Success(coefs) => Right(coefs)
          case Failure(exception) => Left(exception :: Nil)
        }
    }
  }

  override def prepareSignal(env: PreparatorEnv, config: AnalyzeConfiguration)(signal: Signal): Try[Coefs] = {
    for {
      fourier <- env.signalTransform.fourier(env, signal)
      fc@FourierCoefs(firstFreq, fourierCoefs) <- env.featureExtractor.fourierCoefs(env, fourier, config.fourierFeatureConfig)
      MFC(mfcc) <- env.featureExtractor.mfc(env, fc, config.mfcFeatureConfig)
      result = firstFreq +: (fourierCoefs ++ mfcc)
    } yield result
  }
}
