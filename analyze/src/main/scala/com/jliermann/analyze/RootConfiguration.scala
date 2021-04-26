package com.jliermann.analyze

import java.io.File

import com.typesafe.config.Config
import pureconfig.generic.ProductHint
import pureconfig.generic.auto._
import pureconfig.{CamelCase, ConfigFieldMapping, ConfigReader, ConfigSource}

import scala.io.Codec

private[analyze] case class RootConfiguration(analyzeConfiguration: AnalyzeConfiguration,
                                              localConfiguration: LocalConfiguration)

private[analyze] object RootConfiguration {
  implicit def productHint[T]: ProductHint[T] = ProductHint[T](ConfigFieldMapping(CamelCase, CamelCase))

  implicit val fileReader: ConfigReader[File] = ConfigReader[String].map(new File(_))

  implicit val codecReader: ConfigReader[Codec] = ConfigReader[String].map(Codec(_))

  def loadConfigOrThrow(config: Config): RootConfiguration = {
    ConfigSource.fromConfig(config.getConfig("root")).loadOrThrow[RootConfiguration]
  }
}

case class AnalyzeConfiguration(fourierFeatureConfig: FeatureExtractionConfig,
                                mfcFeatureConfig: FeatureExtractionConfig)

case class FeatureExtractionConfig(size: Int, normalize: Option[Boolean])

private[analyze] case class LocalConfiguration(input: FileReaderConfig,
                                               output: File)

private[analyze] case class FileReaderConfig(file: File,
                                             codec: Codec,
                                             sampleSep: String,
                                             numberSep: String)
