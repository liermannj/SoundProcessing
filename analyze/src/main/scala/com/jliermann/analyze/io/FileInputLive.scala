package com.jliermann.analyze.io

import java.io.File

import com.jliermann.analyze.FileReaderConfig
import com.jliermann.analyze.domain.Matrix
import com.jliermann.analyze.domain.SignalTypes.Signal
import com.jliermann.analyze.environment.FileInputEnv
import resource.managed

import scala.io.{Codec, Source}
import scala.util.{Success, Try}

private[analyze] object FileInputLive extends FileInputLive
private[analyze] trait FileInputLive extends FileInput.Service {

  override def readEnregs(env: FileInputEnv, config: FileReaderConfig): Try[Seq[Seq[Signal]]] = {
    for {
      file: Seq[String] <- env.fileInput.readFromFile(config.file, config.codec)
      sampledLines: Seq[Seq[String]] <- Try(file.map(_.split(config.sampleSep).toSeq))
      numberedFile <- Try {
        Matrix(sampledLines)
        .mapCase(_.filterNot(config.ignoredChars.contains))
        .mapCase(tryToDouble(config.numberSep))
        .mapRows(_.filterNot(_.exists(_.isFailure)))
        .mapCase(_.collect { case Success(d) => d})
        .rows
      }
    } yield numberedFile
  }

  override def readFromFile(file: File, codec: Codec): Try[Seq[String]] = Try {
    managed(Source.fromFile(file)(codec)).acquireAndGet(_.getLines().to[Seq])
  }

  private def tryToDouble(sep: String): String => Seq[Try[Double]] = seq =>
    seq.split(sep).map(strDouble => Try(strDouble.toDouble)).toSeq
}
