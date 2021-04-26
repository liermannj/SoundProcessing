package com.jliermann.example.environment

import com.jliermann.analyze.environment.AnalyzeEnvironment
import com.jliermann.example.inputs.{Input, InputLive}
import com.jliermann.sound.environment.SoundEnvironment
import com.jliermann.utils.graph.output
import com.jliermann.utils.graph.output.{Output, OutputLive}

private[example] object EnvironmentLive extends EnvironmentLive
private[example] trait EnvironmentLive
  extends AnalyzeEnvironment
    with SoundEnvironment
    with Input
    with Output {
  override val input: Input.Service = InputLive
  override val output: Output.Service = OutputLive
}
