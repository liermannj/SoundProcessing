package com.jliermann.sound

import com.jliermann.sound.input.{AudioInput, RawAudioSource}
import com.jliermann.sound.process.{AudioWindowing, WordSource}
import com.jliermann.utils.graphs.Graphs
import com.jliermann.utils.output.Output

package object environment {

  type JobEnvironment = AudioInput
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
