package com.jliermann.sound.input
import java.io.{ByteArrayInputStream, InputStream}

import com.jliermann.sound.environment.AudioInputEnv
import javax.sound.sampled.{AudioFormat, TargetDataLine}

object RawAudioSourceMock extends RawAudioSource.Service {
  override def audioInputStream(targetDataLine: TargetDataLine): InputStream = {
    new ByteArrayInputStream("123456789".map(_.toByte).toArray)
  }
}

object AudioInputMock extends AudioInputLive {
  override def unpack(env: AudioInputEnv, bytes: Array[Byte], bvalid: Int, fmt: AudioFormat): Array[Double] = {
    bytes.map(_.toDouble)
  }
}

object AudioInputEnvMock  extends AudioInput with RawAudioSource {
    override val rawAudioSource: RawAudioSource.Service = RawAudioSourceMock

    override val audioInput: AudioInput.Service = AudioInputMock
}
