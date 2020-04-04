package com.jliermann.analyze.environment

import com.jliermann.analyze.math.{FeatureExtractor, FeatureExtractorLive, SignalTransform, SignalTransformLive}

object EnvironmentLive extends EnvironmentLive
trait EnvironmentLive
  extends FeatureExtractor
  with SignalTransform {
  override val featureExtractor: FeatureExtractor.Service = FeatureExtractorLive
  override val signalTransform: SignalTransform.Service = SignalTransformLive
}
