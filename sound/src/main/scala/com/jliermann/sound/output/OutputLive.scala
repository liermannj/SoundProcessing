package com.jliermann.sound.output

import java.io.{File, FileOutputStream}

import akka.stream.IOResult
import akka.stream.scaladsl.{Sink, StreamConverters}
import akka.util.ByteString

import scala.concurrent.Future

private[sound] object OutputLive {

  def sinkToFile(file: File): Sink[Any, Future[IOResult]] = {
    StreamConverters
      .fromOutputStream(() => new FileOutputStream(file))
      .contramap[String](ByteString(_))
      .contramap[Any](any => s"$any\n")
  }

}
