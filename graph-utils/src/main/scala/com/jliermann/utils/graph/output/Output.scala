package com.jliermann.utils.graph.output

import java.io.File

import akka.stream.IOResult
import akka.stream.scaladsl.Sink

import scala.concurrent.Future

trait Output {

  val output: Output.Service

}

object Output {

  trait Service {

    def sinkToFile[T](file: File): Sink[T, Future[IOResult]]

  }
}
