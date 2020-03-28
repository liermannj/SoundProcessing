package com.jliermann.utils.output

import java.io.File

import akka.stream.IOResult
import akka.stream.scaladsl.{FileIO, Flow, Keep, Sink}
import akka.util.ByteString

import scala.concurrent.Future

object OutputLive extends OutputLive
trait OutputLive extends Output.Service {

  def sinkToFile[T](file: File): Sink[T, Future[IOResult]] = {
    Flow[T]
      .map(t => ByteString(s"$t\n"))
      .toMat(FileIO.toPath(file.toPath))(Keep.right)
  }

}
