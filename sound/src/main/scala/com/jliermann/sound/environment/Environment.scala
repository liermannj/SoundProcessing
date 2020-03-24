package com.jliermann.sound.environment

import com.jliermann.sound.input.{AudioInput, AudioInputLive}
import com.jliermann.sound.process.{AudioWindowing, AudioWindowingLive, WordSource, WordSourceLive}
import com.jliermann.utils.graphs.{Graphs, GraphsLive}

private[sound] object EnvironmentLive
  extends AudioInput
    with AudioWindowing
    with Graphs
    with WordSource {
  override val audioInput: AudioInput.Service = AudioInputLive
  override val audioWindowing: AudioWindowing.Service = AudioWindowingLive
  override val graphs: Graphs.Service = GraphsLive
  override val wordSource: WordSource.Service = WordSourceLive
}

object SoundEnvironment extends SoundEnvironment
trait SoundEnvironment
  extends AudioInput
    with AudioWindowing
    with Graphs {
  override val audioInput: AudioInput.Service = AudioInputLive
  override val audioWindowing: AudioWindowing.Service = AudioWindowingLive
  override val graphs: Graphs.Service = GraphsLive
}
