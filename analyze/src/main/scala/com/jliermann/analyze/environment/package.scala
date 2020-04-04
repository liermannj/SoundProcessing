package com.jliermann.analyze

import com.jliermann.analyze.io.FileInput
import com.jliermann.analyze.math.{FeatureExtractor, SignalTransform}
import com.jliermann.analyze.transform.Preparator

package object environment {

  private[analyze] type JobEnvironment = Preparator
    with SignalTransform
    with FeatureExtractor
    with FileInput

  type TransformatorEnv = SignalTransform

  type FourierFeatureExtractorEnv = FeatureExtractor with SignalTransform

  type MFCFeatureExtractorEnv = SignalTransform

  type PreparatorEnv = FeatureExtractor with SignalTransform

  private[analyze] type FileInputEnv = FileInput

}
