package com.jliermann.sound

import java.io.File

import com.jliermann.sound.domain.SamplingRate
import com.typesafe.config.Config
import javax.sound.sampled.AudioFormat
import pureconfig.generic.ProductHint
import pureconfig.generic.auto._
import pureconfig.{CamelCase, ConfigFieldMapping, ConfigReader, ConfigSource}

import scala.concurrent.duration.FiniteDuration

private[sound] case class RootConfiguration(soundConfiguration: SoundConfiguration,
                                            localConfiguration: LocalConfiguration)

object RootConfiguration {
  implicit def productHint[T]: ProductHint[T] = ProductHint[T](ConfigFieldMapping(CamelCase, CamelCase))

  implicit val fileReader: ConfigReader[File] = ConfigReader[String].map(new File(_))

  implicit val finiteDurationReader: ConfigReader[FiniteDuration] = ConfigReader[String].map { s =>
    val (length: String) :: (unit: String) :: _ = s.split(' ').to[List]
    FiniteDuration(length.toLong, unit)
  }

  implicit val audioFormatReader: ConfigReader[AudioFormat] = ConfigReader[Config].map { config =>
    new AudioFormat(config.getDouble("sampleRate").toFloat,
      config.getInt("sampleSizeInBits"),
      config.getInt("channels"),
      config.getBoolean("signed"),
      config.getBoolean("bigEndian"))
  }

  def loadConfigOrThrow(config: Config): RootConfiguration = {
    ConfigSource.fromConfig(config.getConfig("root")).loadOrThrow[RootConfiguration]
  }
}

case class SoundConfiguration(audioFormat: AudioFormat,
                              samplingRate: SamplingRate,
                              overlapping: Double,
                              limitPitchedFrame: Double,
                              silentSeparator: Int)

private[sound] case class LocalConfiguration(outputFile: File)
