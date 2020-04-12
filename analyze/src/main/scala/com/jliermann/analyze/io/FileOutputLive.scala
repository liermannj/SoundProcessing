package com.jliermann.analyze.io

import java.io.{File, PrintWriter}

import scala.util.Try

private[analyze] object FileOutputLive extends FileOutputLive

private[analyze] trait FileOutputLive extends FileOutput.Service {

  override def writeToFile(file: File, data: Seq[String]): Try[Unit] = Try {
    val writer = new PrintWriter(file)
    data
      .map(s => s"$s\n")
      .foreach(writer.write)

    writer.close()
  }
}
