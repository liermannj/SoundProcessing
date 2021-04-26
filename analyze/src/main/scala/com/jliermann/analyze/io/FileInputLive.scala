package com.jliermann.analyze.io

import java.io.File

import com.jliermann.analyze.FileReaderConfig
import com.jliermann.analyze.domain.BidimSeq
import com.jliermann.analyze.domain.SignalTypes.{Enreg, Signal}
import com.jliermann.analyze.environment.FileInputEnv
import com.jliermann.analyze.io.FileInputLive.LineLoadingException
import com.jliermann.analyze.seq.TrySeqOps._
import resource.managed

import scala.io.{Codec, Source}
import scala.util.{Failure, Success, Try}

private[analyze] object FileInputLive extends FileInputLive {

  case class LineLoadingException(file: File, lineIndex: Int) extends UnsupportedOperationException(s"Can't properly load enreg from file $file at line $lineIndex")

}

private[analyze] trait FileInputLive extends FileInput.Service {

  def readEnregs(env: FileInputEnv, config: FileReaderConfig): Seq[Try[Enreg]] = {
    val tryOut = for {
      file: Seq[String] <- env.fileInput.readFromFile(config.file, config.codec)
      sampledLines: Seq[Seq[String]] <- Try(file.map(_.split(config.sampleSep).toSeq))
    } yield BidimSeq(sampledLines)
      .mapCase(tryToSignal(config.numberSep))
      .lines
      .zipWithIndex
      .map { case (enreg, index) => enreg.squash(LineLoadingException(config.file, index)) }

    tryOut match {
      case Success(triedLines) => triedLines
      case Failure(e) => Failure(e) :: Nil
    }
  }

  private[io] def tryToSignal(sep: String): String => Try[Signal] = seq =>
    Try(seq.split(sep).map(strDouble => strDouble.toDouble).toSeq)

  override def readFromFile(file: File, codec: Codec): Try[Seq[String]] = Try {
    managed(Source.fromFile(file)(codec)).acquireAndGet(_.getLines().to[Seq])
  }
}
