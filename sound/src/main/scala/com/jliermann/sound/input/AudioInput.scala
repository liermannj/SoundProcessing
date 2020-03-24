package com.jliermann.sound.input

import akka.stream.IOResult
import akka.stream.scaladsl.Source
import com.jliermann.sound.AudioFormatConfig
import javax.sound.sampled.AudioFormat

import scala.concurrent.Future

trait AudioInput {

  val audioInput: AudioInput.Service

}

object AudioInput {

  final case class SupportedAudioFormat private(format: AudioFormat)

  trait Service {
    def getAudioFormat(audioFormatConfig: AudioFormatConfig): SupportedAudioFormat

    def audioWave(audioFormat: SupportedAudioFormat): Source[Double, Future[IOResult]]
  }

}
