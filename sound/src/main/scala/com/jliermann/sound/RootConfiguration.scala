package com.jliermann.sound

import java.io.File

import com.jliermann.sound.domain.SamplingRate
import com.typesafe.config.Config
import pureconfig.generic.ProductHint
import pureconfig.generic.auto._
import pureconfig.{CamelCase, ConfigFieldMapping, ConfigReader, ConfigSource}

import scala.concurrent.duration.FiniteDuration
import scala.util.Try

private[sound] case class RootConfiguration(soundConfiguration: SoundConfiguration,
                             localConfiguration: LocalConfiguration)

private[sound] object RootConfiguration {
  implicit def productHint[T]: ProductHint[T] = ProductHint[T](ConfigFieldMapping(CamelCase, CamelCase))

  implicit val fileReader: ConfigReader[File] = ConfigReader[String].map(new File(_))

  implicit val finiteDurationReader: ConfigReader[FiniteDuration] = ConfigReader[String].map { s =>
    val (length: String) :: (unit: String) :: _ = s.split(' ').to[List]
    FiniteDuration(length.toLong, unit)
  }

  def loadConfig(config: Config): Try[RootConfiguration] = Try {
    ConfigSource.fromConfig(config.getConfig("root")).loadOrThrow[RootConfiguration]
  }
}

case class SoundConfiguration(audioFormatConfig: AudioFormatConfig,
                              samplingRate: SamplingRate,
                              overlapping: Double,
                              limitPitchedFrame: Double,
                              silentSeparator: Int)

case class AudioFormatConfig(sampleRate: Float,
                             sampleSizeInBits: Int,
                             channels: Int,
                             signed: Boolean,
                             bigEndian: Boolean)

private[sound] case class LocalConfiguration(displaySamplingRate: SamplingRate,
                              outputFile: File)
