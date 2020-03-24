package com.jliermann.sound.input
import akka.stream.IOResult
import akka.stream.scaladsl.Source
import javax.sound.sampled.{AudioFormat, AudioInputStream, AudioSystem, TargetDataLine}
import akka.stream.scaladsl.StreamConverters
import akka.util.ByteString
import com.jliermann.sound.AudioFormatConfig
import com.jliermann.sound.input.AudioInput.SupportedAudioFormat

import scala.concurrent.Future

object AudioInputLive extends AudioInputLive
trait AudioInputLive extends AudioInput.Service {

  override def getAudioFormat(audioFormatConfig: AudioFormatConfig): SupportedAudioFormat = SupportedAudioFormat(
    new AudioFormat(audioFormatConfig.sampleRate,
      audioFormatConfig.sampleSizeInBits,
      audioFormatConfig.channels,
      audioFormatConfig.signed,
      audioFormatConfig.bigEndian))

  override def audioWave(audioFormat: SupportedAudioFormat): Source[Double, Future[IOResult]] = {
    val bufferSize = (audioFormat.format.getSampleRate * audioFormat.format.getFrameSize).toInt * normalBytesFromBits(audioFormat.format.getSampleSizeInBits)
    val targetDataLine: TargetDataLine = AudioSystem.getTargetDataLine(audioFormat.format)

    targetDataLine.open(audioFormat.format)
    targetDataLine.start()
    StreamConverters
      .fromInputStream(() => new AudioInputStream(targetDataLine), bufferSize)
      .flatMapConcat((bs: ByteString) => Source(unpack(bs.toArray, bs.length, audioFormat.format).to))
  }

  private[input] def unpack(bytes: Array[Byte], bvalid: Int, fmt: AudioFormat): Array[Double] = {
    // cf https://github.com/Radiodef/WaveformDemo/blob/master/waveformdemo/WaveformDemo.java
    lazy val buffer_sample_size = bytes.length / normalBytesFromBits(fmt.getSampleSizeInBits)
    lazy val bitsPerSample: Int = fmt.getSampleSizeInBits
    lazy val normalBytes: Int = normalBytesFromBits(bitsPerSample)
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

  private[input] def normalBytesFromBits(bitsPerSample: Int): Int = bitsPerSample + 7 >> 3
}