package com.jliermann.utils.output

import java.io.File

import akka.stream.IOResult
import akka.stream.scaladsl.Sink

import scala.concurrent.Future

trait Output {

  val output: Output.Service

}

object Output {

  trait Service {

    def sinkToFile(file: File): Sink[Any, Future[IOResult]]

  }
}
