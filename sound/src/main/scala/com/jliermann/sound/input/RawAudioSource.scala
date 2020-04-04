package com.jliermann.sound.input

import java.io.InputStream

import javax.sound.sampled.TargetDataLine

trait RawAudioSource {

  val rawAudioSource: RawAudioSource.Service

}

object RawAudioSource {

  trait Service {

    def audioInputStream(targetDataLine: TargetDataLine): InputStream

  }

}
