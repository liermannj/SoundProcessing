package com.jliermann.example

import com.jliermann.analyze.AnalyzeConfiguration
import com.jliermann.sound.SoundConfiguration
import com.typesafe.config.Config
import pureconfig.generic.ProductHint
import pureconfig.generic.auto._
import pureconfig.{CamelCase, ConfigFieldMapping, ConfigReader, ConfigSource}

import java.io.File
import javax.sound.sampled.AudioFormat
import scala.concurrent.duration.FiniteDuration
import scala.io.Codec

private[example] case class RootConfiguration(featureConfiguration: FeatureConfiguration,
                                              localConfiguration: LocalConfiguration)

// TODO: factorize readers
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

  implicit val codecReader: ConfigReader[Codec] = ConfigReader[String].map(Codec(_))

  def loadConfigOrThrow(config: Config): RootConfiguration = {
    ConfigSource.fromConfig(config.getConfig("root")).loadOrThrow[RootConfiguration]
  }
}

case class FeatureConfiguration(soundConfiguration: SoundConfiguration,
                                analyzeConfiguration: AnalyzeConfiguration)

case class LocalConfiguration(outputFile: File, predictor: PredictorConfig)

case class PredictorConfig(script: File, file: File, featureSep: String)

