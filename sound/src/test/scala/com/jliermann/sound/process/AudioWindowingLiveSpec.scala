package com.jliermann.sound.process

import akka.stream.scaladsl.Source
import com.jliermann.sound.domain.{Frame, FrameGens, SamplingRate, SpokenTag}
import com.jliermann.utils.test.{PropTest, StreamTest}

import scala.collection.immutable
import scala.concurrent.duration._

class AudioWindowingLiveSpec extends PropTest with StreamTest with FrameGens {

  "keepCoherentChunk" should "concatenate chunks of pitched data" in {
    def labelPitched(d: Double): SpokenTag[Double] = SpokenTag.label(d*d - 1)(Frame(Seq(d)))
    def labelSilent(d: Double): SpokenTag[Double] = SpokenTag.label(d*d + 1)(Frame(Seq(d)))
    val input = Source(immutable.Iterable(
      labelPitched(2), labelSilent(1), labelPitched(3), labelSilent(1), labelSilent(1), labelSilent(1),
      labelPitched(2),labelPitched(2),labelSilent(1), labelSilent(1),labelPitched(2)))

    val result = awaitResult(10.second)(input.via(AudioWindowingLive.keepCoherentChunk(AudioWindowingEnvMock, 2)))

    val expected = Seq(Seq(labelPitched(2), labelSilent(1), labelPitched(3)), Seq(labelPitched(2),labelPitched(2)))
    result.zip(expected)
      .foreach { case (r, e) => r shouldBe e}
  }

  "window" should "return arrays of the length given by the sampling rate" in {
    val input = arbSource[Double](100).throttle(1, 0.01.second)
    val result: Seq[Frame[Double]] = awaitResult(10.second) {
      input.via(AudioWindowingLive.window(AudioWindowingEnvMock, SamplingRate(10, 0.1.second), 0D))
    }

    result should have length 10
    result.foreach(_.xs should have length 10)
  }

  it should "get the correct amount of element from the last frame" in {
    val input = arbSource[Double](100).throttle(1, 0.01.second)
    val result: Seq[Frame[Double]] = awaitResult(10.second) {
      input.via(AudioWindowingLive.window(AudioWindowingEnvMock, SamplingRate(10, 0.1.second), 0.1))
    }

    val resultMinusOne =  Frame(Seq.fill(10)(0D)) +: result.reverse.tail.reverse
    result.zip(resultMinusOne).foreach { case (r, rM1) => r.xs.head shouldBe rM1.xs.reverse.head}
  }

  "labelPitched" should "apply a SpokenTag over a stream" in forAll { limit: Double =>
    val input = arbSource[Frame[Double]](100)
    val result = awaitResult(10.second)(input.via(AudioWindowingLive.labelPitched(limit)))

    result.foreach {
      case _: SpokenTag[_] =>
      case _ => fail()
    }
    succeed
  }

  "emphasizing" should "be comprised between ]0;1]" in forAll { (length: Int, pointOfApply: Int) =>
    whenever(length != 0) {
      val result = AudioWindowingLive.emphasizing(length)(pointOfApply)
      result should be > 0D
      result should be <= 1D
    }
  }

  "hamming" should "apply the emphasize over an array, diminishing total value" in forAll { input: Seq[Double] =>
    val inputSum = input.map(math.abs).sum
    val resultSum = AudioWindowingLive.hamming(input).map(math.abs).sum
    input match {
      case Nil => succeed
      case _ => inputSum should be > resultSum
    }
  }
}
