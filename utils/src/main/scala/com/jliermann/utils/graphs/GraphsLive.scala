package com.jliermann.utils.graphs

import akka.NotUsed
import akka.stream.FlowShape
import akka.stream.scaladsl.{Broadcast, Flow, GraphDSL, MergePreferred, MergePrioritized, Source, Unzip, Zip}

import scala.collection.immutable
import scala.concurrent.duration.FiniteDuration

object GraphsLive extends GraphsLive

trait GraphsLive extends Graphs.Service {

  override def regularize[T](timeUnit: FiniteDuration)(implicit num: Numeric[T]): Flow[T, Double, NotUsed] = {
    Flow[T]
      .conflateWithSeed(Seq(_))(_ :+ _)
      .map(l => num.toDouble(l.sum) / l.length)
      .throttle(1, timeUnit)
  }

  override def loopLast[T](zero: T)(acc: (T, T) => T): Flow[T, T, NotUsed] = Flow.fromGraph {
    GraphDSL.create() { implicit b =>
      import GraphDSL.Implicits._

      val in = b.add(Flow[T])
      val constant = Source(immutable.Iterable(zero))
      val mergeT0 = b.add(MergePreferred[T](1))
      val broadcast = b.add(Broadcast[T](2))
      val zip = b.add(Zip[T, T])
      val operate = b.add(Flow[(T, T)].map(acc.tupled))

      /*___________________________________*/ in ~> zip.in0
      /*__________________*/ constant ~> mergeT0 ~> zip.in1
      zip.out ~> operate ~> broadcast ~> mergeT0

      FlowShape(in.in, broadcast.out(1))
    }
  }

  override def foldUntil[T, U](zero: U)(acc: (T, U) => U, stop: U => Boolean): Flow[T, U, NotUsed] = Flow.fromGraph {
    GraphDSL.create() { implicit b =>
      import GraphDSL.Implicits._

      val enhancedAcc: (T, U) => (U, U) = (el: T, accu: U) => if(stop(accu)) (acc(el, zero), accu) else (acc(el, accu), zero)

      val in = b.add(Flow[T])
      val transfer = b.add(Unzip[U, U])
      val constant = Source(immutable.Iterable(zero))
      val mergeT0 = b.add(MergePrioritized[U](Seq(2, 1)))
      val zip = b.add(Zip[T, U])
      val operate = b.add(Flow[(T, U)].map(enhancedAcc.tupled))

      /*_________________*/ in ~> zip.in0 ; zip.out ~> operate ~> transfer.in
      /*____________*/ mergeT0 ~> zip.in1
      /**/ constant ~> mergeT0.in(1)
      transfer.out0 ~> mergeT0.in(0)

      FlowShape(in.in, transfer.out1)
    }
  }.filter(_ != zero)
}

