package com.jliermann.sound.process

import akka.NotUsed
import akka.stream.scaladsl.Flow
import com.jliermann.sound.domain.{Frame, SamplingRate, SpokenTag}
import com.jliermann.sound.environment.WindowingEnv

object AudioWindowingLive extends AudioWindowingLive {
  final private[process] def emphasizing(totalLength: Int): Int => Double = i => 0.54 - 0.46 * Math.cos(2 * Math.PI * i.toDouble / totalLength.toDouble)

  @scala.annotation.tailrec
  final private[process] def trimSilentHead(input: Seq[SpokenTag[Double]]): Seq[SpokenTag[Double]] = {
    if(input.headOption.exists(_.isSilent)) trimSilentHead(input.tail)
    else input
  }

  final private[process] def trimSilent(input: Seq[SpokenTag[Double]]): Seq[SpokenTag[Double]] = {
    trimSilentHead(trimSilentHead(input).reverse).reverse
  }
}

trait AudioWindowingLive extends AudioWindowing.Service {
  import AudioWindowingLive._

  override def keepCoherentChunk(env: WindowingEnv, silentSep: Int): Flow[SpokenTag[Double], Seq[SpokenTag[Double]], NotUsed] = {
    val acc = (el: SpokenTag[Double], accu: Seq[SpokenTag[Double]]) => accu :+ el
    val stop = (accu: Seq[SpokenTag[Double]]) => !accu.slice(accu.length - silentSep, accu.length).exists(_.isPitched) && accu.exists(_.isPitched)
    env.graphs
      .foldUntil(Seq.empty[SpokenTag[Double]])(acc, stop)
      .map(trimSilent)
  }

  override def window(env: WindowingEnv, samplingRate: SamplingRate, overlapping: Double): Flow[Double, Frame[Double], NotUsed] = {
    val overlapRecords = (samplingRate.samples * overlapping).toInt
    val newRecords = (samplingRate.samples - overlapRecords).toInt
    val loop = env.graphs.loopLast[Seq[Double]](Nil) {
      case (input, Nil) => List.fill(overlapRecords)(0D) ++ input
      case (input, previous) => previous.slice(newRecords, previous.length) ++ input
    }.map(env.audioWindowing.hamming)

    env.graphs
      .regularize[Double](samplingRate.ratePerSample)
      .grouped(newRecords)
      .via(loop)
      .map(Frame(_))
  }

  override def labelPitched(floor: Double): Flow[Frame[Double], SpokenTag[Double], NotUsed] = {
    Flow[Frame[Double]].map(SpokenTag.label(floor))
  }

  override def hamming(rawArray: Seq[Double]): Seq[Double] = {
    val emphasize = emphasizing(rawArray.length)
    rawArray
      .zipWithIndex
      .map { case (value, index) => value * emphasize(index) }
  }
}
