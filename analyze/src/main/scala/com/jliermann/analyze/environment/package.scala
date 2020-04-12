package com.jliermann.analyze

import com.jliermann.analyze.io.{FileInput, FileOutput}
import com.jliermann.analyze.math.{FeatureExtractor, RawMath, SignalTransform}
import com.jliermann.analyze.preprocess.Preparator

package object environment {

  type TransformatorEnv = SignalTransform
  type FourierFeatureExtractorEnv = FeatureExtractor with SignalTransform with RawMath
  type MFCFeatureExtractorEnv = SignalTransform with RawMath
  type PreparatorEnv = FeatureExtractor with SignalTransform with RawMath
  type EnregPreparatorEnv = FeatureExtractor with SignalTransform with RawMath with Preparator
  type AggregateEnv = RawMath
  private[analyze] type JobEnvironment = Preparator
    with SignalTransform
    with FeatureExtractor
    with FileInput
    with FileOutput
    with RawMath
  private[analyze] type FileInputEnv = FileInput

}
