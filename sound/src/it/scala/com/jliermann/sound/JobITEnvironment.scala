package com.jliermann.sound

import com.jliermann.sound.FixtureIT._
import com.jliermann.sound.environment.EnvironmentLive
import com.jliermann.sound.input.{RawAudioSource, RawAudioSourceLive}
import javax.sound.sampled.{AudioInputStream, AudioSystem, TargetDataLine}

object JobITMockRawAudioSource extends RawAudioSourceLive {
  override def audioInputStream(targetDataLine: TargetDataLine): AudioInputStream = {
    AudioSystem.getAudioInputStream(getClass.getClassLoader.getResource(AudioFile.toString))
  }
}

object JobITEnvironment extends EnvironmentLive {

  override val rawAudioSource: RawAudioSource.Service = JobITMockRawAudioSource
}
