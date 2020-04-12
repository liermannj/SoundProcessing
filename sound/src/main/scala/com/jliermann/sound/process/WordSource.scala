package com.jliermann.sound.process

import akka.stream.IOResult
import akka.stream.scaladsl.Source
import com.jliermann.sound.SoundConfiguration
import com.jliermann.sound.domain.SpokenTag
import com.jliermann.sound.environment.WordSourceEnvironment
import javax.sound.sampled.TargetDataLine

import scala.concurrent.Future

trait WordSource {

  val wordSource: WordSource.Service

}

object WordSource {

  trait Service {

    /**
     * Create a source composed of a refined sound ingestion, separating itself consecutive spoken sequences
     * The composition of each Frame is defined by the config parameter, as well as the sensibility of the source to distinguish spoken sequences
     *
     * @return elements corresponding to individual spoken enregs, composed of a variable number of Spoken Frames or Silent Frames
     *
     */
    def source(env: WordSourceEnvironment, config: SoundConfiguration, tdl: TargetDataLine): Source[Seq[SpokenTag[Double]], Future[IOResult]]

  }

}