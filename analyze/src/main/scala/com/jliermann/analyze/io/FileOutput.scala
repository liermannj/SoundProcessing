package com.jliermann.analyze.io

import com.jliermann.analyze.FileConfig
import com.jliermann.analyze.domain.SignalTypes.Coefs

import java.io.File
import scala.util.Try

private[analyze] trait FileOutput {
  val fileOutput: FileOutput.Service
}

private[analyze] object FileOutput {

  trait Service {
    def writeToFile(fileConfig: FileConfig, data: Seq[Seq[Coefs]]): Try[Unit]
  }

}
