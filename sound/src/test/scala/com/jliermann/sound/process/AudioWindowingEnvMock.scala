package com.jliermann.sound.process

import akka.NotUsed
import akka.stream.scaladsl.Flow
import com.jliermann.utils.graphs.{Graphs, GraphsLive}

import scala.concurrent.duration.FiniteDuration

object AudioWindowingEnvMock extends Graphs with AudioWindowing {
  override val graphs: Graphs.Service = GraphsMock
  override val audioWindowing: AudioWindowing.Service = AudioWindowingMock
}

object GraphsMock extends GraphsLive {

  override def regularize[T](timeUnit: FiniteDuration)(implicit num: Numeric[T]): Flow[T, Double, NotUsed] = {
    Flow[T]
      .conflateWithSeed(num.toDouble)((_, r) => num.toDouble(r))
      .throttle(1, timeUnit)
  }
}

object AudioWindowingMock extends AudioWindowingLive {
  override def hamming(rawArray: Seq[Double]): Seq[Double] = rawArray
}
