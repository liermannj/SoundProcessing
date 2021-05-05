package com.jliermann.analyze.io

import java.io.{BufferedOutputStream, File, FileOutputStream, OutputStream}
import com.jliermann.analyze.{EOL, FileConfig}
import com.jliermann.analyze.domain.SignalTypes.Coefs

import scala.util.Try

private[analyze] object FileOutputLive extends FileOutputLive
private[analyze] trait FileOutputLive extends FileOutput.Service {

  override def writeToFile(fileConfig: FileConfig, data: Seq[Seq[Coefs]]): Try[Unit] = provideWriter(fileConfig.file) {
    writer =>
      Try {
        if (!fileConfig.file.exists()) fileConfig.file.createNewFile()
        data
          .map((enreg: Seq[Coefs]) => enreg
            .map((frame: Coefs) => frame.mkString(fileConfig.numberSep))
            .mkString(fileConfig.sampleSep))
          .map((s: String) => s"$s$EOL".getBytes(fileConfig.codec.charSet))
          .foreach(writer.write)

        writer.flush()
      }
  }

  private[io] def provideWriter[T](file: File)(f: OutputStream => T): T = {
    val writer = new BufferedOutputStream(new FileOutputStream(file))
    try f(writer) finally writer.close()
  }
}
