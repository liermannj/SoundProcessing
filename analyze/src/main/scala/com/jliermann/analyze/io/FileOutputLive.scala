package com.jliermann.analyze.io

import java.io.{BufferedOutputStream, File, FileOutputStream, OutputStream}

import com.jliermann.analyze.EOL

import scala.util.Try

private[analyze] object FileOutputLive extends FileOutputLive

private[analyze] trait FileOutputLive extends FileOutput.Service {

  override def writeToFile(file: File, data: Seq[String]): Try[Unit] = provideWriter(file) {
    writer =>
      Try {
        if (!file.exists()) file.createNewFile()
        data
          .map(s => s"$s$EOL".getBytes)
          .foreach(writer.write)

        writer.flush()
      }
  }

  private[io] def provideWriter[T](file: File)(f: OutputStream => T): T = {
    val writer = new BufferedOutputStream(new FileOutputStream(file))
    try f(writer) finally writer.close()
  }
}
