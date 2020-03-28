package com.jliermann.utils.test

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.scaladsl.{Sink, Source}
import org.scalacheck.{Arbitrary, Gen}
import org.scalatest.{FlatSpec, Matchers}

import scala.collection.immutable
import scala.concurrent.Await
import scala.concurrent.duration.FiniteDuration

trait StreamTest extends FlatSpec with Matchers {

  implicit lazy val actorSystem: ActorSystem = ActorSystem("TestSystem")

  def awaitResult[T](timeout: FiniteDuration)(source: Source[T, NotUsed]): Seq[T] = {
    Await.result(source.runWith(Sink.seq), timeout)
  }

  def arbSource[T: Arbitrary](size: Int): Source[T, NotUsed] = {
    Source(Gen.listOfN(size, Arbitrary.arbitrary[T]).sample.getOrElse(Seq.empty[T]).to[immutable.Iterable])
  }

}
