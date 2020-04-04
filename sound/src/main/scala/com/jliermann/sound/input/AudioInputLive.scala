package com.jliermann.sound.input
import akka.stream.IOResult
import akka.stream.scaladsl.{Source, StreamConverters}
import akka.util.ByteString
import com.jliermann.sound.environment.AudioInputEnv
import javax.sound.sampled.{AudioFormat, TargetDataLine}

import scala.concurrent.Future

object AudioInputLive extends AudioInputLive
trait AudioInputLive extends AudioInput.Service {

  override def audioWave(env: AudioInputEnv, audioFormat: AudioFormat, tdl: TargetDataLine): Source[Double, Future[IOResult]] = {
    val bufferSize = (audioFormat.getSampleRate * audioFormat.getFrameSize).toInt * normalBytesFromBits(audioFormat.getSampleSizeInBits)

    StreamConverters
      .fromInputStream(() => env.rawAudioSource.audioInputStream(tdl), bufferSize)
      .flatMapConcat((bs: ByteString) => Source(env.audioInput.unpack(env, bs.toArray, bs.length, audioFormat).to))
  }

  def unpack(env: AudioInputEnv, bytes: Array[Byte], bvalid: Int, fmt: AudioFormat): Array[Double] = {
    // cf https://github.com/Radiodef/WaveformDemo/blob/master/waveformdemo/WaveformDemo.java
    lazy val buffer_sample_size = bytes.length / env.audioInput.normalBytesFromBits(fmt.getSampleSizeInBits)
    lazy val bitsPerSample: Int = fmt.getSampleSizeInBits
    lazy val normalBytes: Int = env.audioInput.normalBytesFromBits(bitsPerSample)
    lazy val fullScale: Long = Math.pow(2.0, bitsPerSample - 1).toLong
    lazy val signShit: Long = 64L - bitsPerSample

    Array
      .fill(buffer_sample_size * fmt.getChannels)(0L) // transfer
      .zipWithIndex
      .map {
        case (tr, i) if i < bvalid =>
          val byteSlice: Array[Byte] = bytes.slice(i * normalBytes, (i + 1) * normalBytes)
          (if(fmt.isBigEndian) byteSlice.reverse else byteSlice) // if bigEndian, reverse
            .zipWithIndex
            .foldLeft(tr) { case (acc, (b, indexOfB)) => acc | ((b & 0xffL) << (8 * indexOfB)) }
        case (tr, _) => tr
      }
      .map(raw =>
        if(fmt.getEncoding == AudioFormat.Encoding.PCM_SIGNED) (raw << signShit) >> signShit // sign shift
        else raw - fullScale) // unsign center
      .map(_ / fullScale.toDouble)
  }

  def normalBytesFromBits(bitsPerSample: Int): Int = bitsPerSample >> 3
}