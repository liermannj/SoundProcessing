package com.jliermann.analyze.io

import java.io.File

import com.jliermann.analyze.FileReaderConfig
import com.jliermann.analyze.domain.SignalTypes.Signal
import com.jliermann.analyze.environment.FileInputEnv

import scala.io.Codec
import scala.util.Try


private[analyze] trait FileInput {
  val fileInput: FileInput.Service
}

private[analyze] object FileInput {

  trait Service {

    def readEnregs(env: FileInputEnv, config: FileReaderConfig): Try[Seq[Seq[Signal]]]

    def readFromFile(file: File, codec: Codec): Try[Seq[String]]
  }

}