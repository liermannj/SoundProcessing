package com.jliermann.analyze.environment

import com.jliermann.analyze.io.{FileInput, FileInputLive, FileOutput, FileOutputLive}
import com.jliermann.analyze.math._
import com.jliermann.analyze.preprocess.{Preparator, PreparatorLive}

private[analyze] object EnvironmentLive extends EnvironmentLive

private[analyze] trait EnvironmentLive
  extends FeatureExtractor
    with SignalTransform
    with Preparator
    with FileInput
    with FileOutput
    with RawMath {
  override val featureExtractor: FeatureExtractor.Service = FeatureExtractorLive
  override val signalTransform: SignalTransform.Service = SignalTransformLive
  override val preparator: Preparator.Service = PreparatorLive
  override val fileInput: FileInput.Service = FileInputLive
  override val fileOutput: FileOutput.Service = FileOutputLive
  override val rawMath: RawMath.Service = RawMathLive
}


