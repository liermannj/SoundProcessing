package com.jliermann.analyze.io
import java.io.{BufferedWriter, File, FileWriter}

import scala.util.Try

private[analyze] object FileOutputLive extends FileOutputLive
private[analyze] trait FileOutputLive extends FileOutput.Service {

  override def writeToFile(file: File, data: Seq[String]): Try[Unit] = Try {
    val writer = new BufferedWriter(new FileWriter(file))
    data.foreach(writer.write)
  }
}
