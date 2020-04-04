package com.jliermann.utils.graphs

import akka.NotUsed
import akka.stream.scaladsl.Flow

import scala.concurrent.duration.FiniteDuration

trait Graphs {

  val graphs: Graphs.Service

}

object Graphs {

  trait Service {

    def regularize[T: Numeric](timeUnit: FiniteDuration): Flow[T, Double, NotUsed]

    def loopLast[T, U](zero: U)(acc: (T, U) => U): Flow[T, U, NotUsed]

    def foldUntil[T, U](zero: U)(acc: (T, U) => U, stop: U => Boolean): Flow[T, U, NotUsed]
  }

}
