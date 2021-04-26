package com.jliermann.analyze.preprocess

import com.jliermann.analyze.AnalyzeConfiguration
import com.jliermann.analyze.domain.Matrix
import com.jliermann.analyze.domain.SignalTypes._
import com.jliermann.analyze.environment.{EnregPreparatorEnv, PreparatorEnv}
import com.jliermann.analyze.seq.NumericSeqOps._
import com.jliermann.analyze.seq.TrySeqOps._

import scala.util.Try

object PreparatorLive extends PreparatorLive {

  case class ImproperEnregException(enreg: Enreg) extends ArithmeticException(s"unable to compute footprint for enreg $enreg")

}

trait PreparatorLive extends Preparator.Service {

  import PreparatorLive._

  override def prepareEnreg(env: EnregPreparatorEnv, config: AnalyzeConfiguration)(enreg: Enreg): Try[Coefs] = {
    for {
      unitProcessed: Seq[Coefs] <- enreg
        .map(env.preparator.prepareSignal(env, config))
        .squash(ImproperEnregException(enreg))
      if unitProcessed.nonEmpty
      matrix <- Matrix(unitProcessed)
      coefs <- Try(matrix.transpose.lines.flatMap(xs => xs.mean :: xs.stdDev :: Nil))
    } yield coefs
  }

  override def prepareSignal(env: PreparatorEnv, config: AnalyzeConfiguration)(signal: Signal): Try[Coefs] = {
    for {
      fourier <- env.signalTransform.fourier(env, signal)
      fc@FourierCoefs(fourierCoefs) <- env.featureExtractor.fourierCoefs(env, fourier, config.fourierFeatures)
      MFC(mfcc) <- env.featureExtractor.mfc(env, fc, config.mfcFeatures)
      result = fourierCoefs ++ mfcc
    } yield config
      .normalize
      .filter(identity)
      .fold(result)(_ => env.signalTransform.normalize(result))
  }
}
