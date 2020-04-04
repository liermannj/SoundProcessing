package com.jliermann.analyze.transform
import com.jliermann.analyze.AnalyzeConfiguration
import com.jliermann.analyze.domain.SignalTypes.{Coefs, FourierCoefs, MFC, Signal}
import com.jliermann.analyze.environment.PreparatorEnv

import scala.util.Try

object PreparatorLive extends PreparatorLive
trait PreparatorLive extends Preparator.Service {
  override def prepareSignal(env: PreparatorEnv, signal: Signal, config: AnalyzeConfiguration): Try[Coefs] = {
    for {
      fourier <- env.signalTransform.fourier(env, signal)
      fc @ FourierCoefs(firstFreq, fourierCoefs) <- env.featureExtractor.fourierCoefs(env, fourier, config.fourierFeatureConfig)
      MFC(mfcc) <- env.featureExtractor.mfc(env, fc, config.mfcFeatureConfig)
      result = firstFreq +: (fourierCoefs ++ mfcc)
    } yield result
  }
}
