package com.jliermann.sound.environment

import com.jliermann.sound.input.{AudioInput, AudioInputLive, RawAudioSource, RawAudioSourceLive}
import com.jliermann.sound.process.{AudioWindowing, AudioWindowingLive, WordSource, WordSourceLive}
import com.jliermann.utils.graph.graphs.{Graphs, GraphsLive}
import com.jliermann.utils.graph.output.{Output, OutputLive}

private[sound] object EnvironmentLive extends EnvironmentLive

private[sound] trait EnvironmentLive
  extends SoundEnvironment
    with Output {
  override val audioInput: AudioInput.Service = AudioInputLive
  override val audioWindowing: AudioWindowing.Service = AudioWindowingLive
  override val graphs: Graphs.Service = GraphsLive
  override val output: Output.Service = OutputLive
}

object SoundEnvironment extends SoundEnvironment

trait SoundEnvironment
  extends AudioInput
    with RawAudioSource
    with AudioWindowing
    with WordSource
    with Graphs {
  override val audioInput: AudioInput.Service = AudioInputLive
  override val audioWindowing: AudioWindowing.Service = AudioWindowingLive
  override val graphs: Graphs.Service = GraphsLive
  override val rawAudioSource: RawAudioSource.Service = RawAudioSourceLive
  override val wordSource: WordSource.Service = WordSourceLive
}
