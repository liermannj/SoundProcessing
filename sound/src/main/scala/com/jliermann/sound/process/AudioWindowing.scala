package com.jliermann.sound.process

import akka.NotUsed
import akka.stream.scaladsl.Flow
import com.jliermann.sound.domain.{Frame, SamplingRate, SpokenTag}
import com.jliermann.sound.environment.WindowingEnv

trait AudioWindowing {

  val audioWindowing: AudioWindowing.Service

}

object AudioWindowing {

  trait Service {
    def keepCoherentChunk(env: WindowingEnv, silentSep: Int): Flow[SpokenTag[Double], Seq[SpokenTag[Double]], NotUsed]

    def window(env: WindowingEnv, samplingRate: SamplingRate, overlapping: Double): Flow[Double, Frame[Double], NotUsed]

    def labelPitched(floor: Double): Flow[Frame[Double], SpokenTag[Double], NotUsed]

    def hamming(rawArray: Seq[Double]): Seq[Double]

  }

}
