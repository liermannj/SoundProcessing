package com.jliermann.sound.input

import akka.stream.IOResult
import akka.stream.scaladsl.Source
import com.jliermann.sound.environment.AudioInputEnv
import javax.sound.sampled.{AudioFormat, TargetDataLine}

import scala.concurrent.Future

trait AudioInput {

  val audioInput: AudioInput.Service

}

object AudioInput {

  trait Service {
    def audioWave(env: AudioInputEnv, audioFormat: AudioFormat, tdl: TargetDataLine): Source[Double, Future[IOResult]]

    def unpack(env: AudioInputEnv, bytes: Array[Byte], bvalid: Int, fmt: AudioFormat): Array[Double]

    def normalBytesFromBits(bitsPerSample: Int): Int
  }

}
