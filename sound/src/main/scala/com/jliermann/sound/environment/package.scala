package com.jliermann.sound

import com.jliermann.sound.input.{AudioInput, RawAudioSource}
import com.jliermann.sound.process.{AudioWindowing, WordSource}
import com.jliermann.utils.graph.graphs.Graphs
import com.jliermann.utils.graph.output.Output

package object environment {

  private[sound] type JobEnvironment = AudioInput
    with RawAudioSource
    with AudioWindowing
    with Graphs
    with WordSource
    with Output

  type WordSourceEnvironment = AudioInput
    with RawAudioSource
    with AudioWindowing
    with Graphs

  type WindowingEnv = AudioWindowing with Graphs

  type AudioInputEnv = RawAudioSource with AudioInput

}
