package com.jliermann.sound

import com.jliermann.sound.input.AudioInput
import com.jliermann.sound.process.{AudioWindowing, WordSource}
import com.jliermann.utils.graphs.Graphs
import com.jliermann.utils.output.Output

package object environment {

  type JobEnvironment = AudioInput
    with AudioWindowing
    with Graphs
    with WordSource
    with Output

  type WordSourceEnvironment = AudioInput
    with AudioWindowing
    with Graphs

  type WindowingEnv = AudioWindowing with Graphs

  type GraphsEnv = Graphs

}
