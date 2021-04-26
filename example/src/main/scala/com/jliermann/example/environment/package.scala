package com.jliermann.example

import com.jliermann.analyze.environment.AnalyzeEnvironment
import com.jliermann.example.inputs.Input
import com.jliermann.sound.environment.SoundEnvironment
import com.jliermann.utils.graph.output.Output

package object environment {

  type FeatureEnvironment = SoundEnvironment
    with AnalyzeEnvironment

  type InputEnvironment = FeatureEnvironment with Input

  type Environment = SoundEnvironment
    with AnalyzeEnvironment
    with Input
    with Output

}
