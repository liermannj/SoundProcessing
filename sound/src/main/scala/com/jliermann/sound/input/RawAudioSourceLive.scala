package com.jliermann.sound.input

import javax.sound.sampled.{AudioInputStream, TargetDataLine}

object RawAudioSourceLive extends RawAudioSourceLive
trait RawAudioSourceLive extends RawAudioSource.Service {

  override def audioInputStream(targetDataLine: TargetDataLine): AudioInputStream = {
    new AudioInputStream(targetDataLine)
  }
}
