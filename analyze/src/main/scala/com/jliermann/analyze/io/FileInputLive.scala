package com.jliermann.analyze.io

import java.io.File

import com.jliermann.analyze.FileReaderConfig
import com.jliermann.analyze.domain.SignalTypes.Signal
import com.jliermann.analyze.environment.FileInputEnv
import resource.managed

import scala.io.{Codec, Source}
import scala.util.{Success, Try}

private[analyze] object FileInputLive extends FileInputLive
private[analyze] trait FileInputLive extends FileInput.Service {
  override def readSignal(env: FileInputEnv, config: FileReaderConfig): Try[Seq[Seq[Signal]]] = {
    for {
      file: Seq[String] <- env.fileInput.readFromFile(config.file, config.codec)
      sampledLines: Seq[Seq[String]] <- Try(file.map(_.split(config.sampleSep).toSeq))
      numberedFile <- Try {
        sampledLines.map(_
          .map(tryToDouble(config.numberSep))
          .filterNot(_.exists(_.isFailure))
          .map(_.collect { case Success(d) => d}))
      }
    } yield numberedFile
  }

  override def readFromFile(file: File, codec: Codec): Try[Seq[String]] = Try {
    managed(Source.fromFile(file)(codec)).acquireAndGet(_.getLines().to[Seq])
  }

  private def tryToDouble(sep: String): String => Seq[Try[Double]] = seq =>
    seq.split(sep).map(strDouble => Try(strDouble.toDouble)).toSeq
}
