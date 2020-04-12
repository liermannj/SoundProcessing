package com.jliermann.utils.graph.graphs

import akka.stream.scaladsl.Source
import com.jliermann.utils.test.StreamTest

import scala.collection.immutable
import scala.concurrent.duration._

class GraphsLiveSpec extends StreamTest {

  "regularize" should "regularly send input" in {
    val input = arbSource[Double](math.pow(10, 3).toInt)
    val result = awaitResult(1.5.second)(input.via(GraphsLive.regularize[Double](0.5.second)))

    result.length should be <= 3
  }

  "loopLast" should "apply a function of the last two elements over the last" in {
    val input = Source.repeat(1)
      .via(GraphsLive.loopLast(0)(_ + _))
      .take(10)

    val expected = 1 to 10
    val result = awaitResult(10.second)(input)

    result should contain theSameElementsInOrderAs expected
  }

  "foldUntil" should "keep accumulating and diverge when stop condition is met" in {
    val stopValue = "STOP"
    val input = Source(immutable.Iterable("keep on", "continue", "don't stop", stopValue, stopValue, stopValue, "continue", "my string", stopValue, "a word"))

    val acc = (value: String, accu: Seq[String]) => accu :+ value
    val stop = (accu: Seq[String]) => accu.lastOption.contains(stopValue) && accu.exists(_ != stopValue)
    val result = awaitResult(10.second)(input.via(GraphsLive.foldUntil(Seq.empty[String])(acc, stop)))

    val expected = Seq(Seq("keep on", "continue", "don't stop", stopValue), Seq(stopValue, stopValue, "continue", "my string", stopValue))
    result.zip(expected)
      .foreach { case (r, e) => r should contain theSameElementsInOrderAs e }
  }
}
