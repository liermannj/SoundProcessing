package com.jliermann.analyze.io

import java.io.File

import scala.util.Try

private[analyze] trait FileOutput {
  val fileOutput: FileOutput.Service
}

private[analyze] object FileOutput {

  trait Service {
    def writeToFile(file: File, data: Seq[String]): Try[Unit]
  }

}
