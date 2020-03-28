package com.jliermann.utils.output

import java.io.File

import akka.stream.IOResult
import akka.stream.scaladsl.{FileIO, Flow, Keep, Sink}
import akka.util.ByteString

import scala.concurrent.Future

object OutputLive extends OutputLive
trait OutputLive extends Output.Service {

  def sinkToFile(file: File): Sink[Any, Future[IOResult]] = {
    Flow[Any]
      .map(any => ByteString(s"$any\n"))
      .toMat(FileIO.toPath(file.toPath))(Keep.right)
  }

}
