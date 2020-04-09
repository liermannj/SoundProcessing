package com.jliermann.analyze

import com.jliermann.analyze.io.{FileInput, FileOutput}
import com.jliermann.analyze.math.{FeatureExtractor, SignalTransform}
import com.jliermann.analyze.preprocess.Preparator

package object environment {

  private[analyze] type JobEnvironment = Preparator
    with SignalTransform
    with FeatureExtractor
    with FileInput
    with FileOutput

  type TransformatorEnv = SignalTransform

  type FourierFeatureExtractorEnv = FeatureExtractor with SignalTransform

  type MFCFeatureExtractorEnv = SignalTransform

  type PreparatorEnv = FeatureExtractor with SignalTransform

  type EnregPreparatorEnv = FeatureExtractor with SignalTransform with Preparator

  private[analyze] type FileInputEnv = FileInput

}
