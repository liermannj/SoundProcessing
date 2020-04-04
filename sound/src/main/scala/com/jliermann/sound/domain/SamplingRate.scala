package com.jliermann.sound.domain

import java.util.concurrent.TimeUnit

import scala.concurrent.duration.FiniteDuration

case class SamplingRate(samples: Long, timeUnit: FiniteDuration) {
  val ratePerSample: FiniteDuration = {
    new FiniteDuration(timeUnit.toNanos / samples, TimeUnit.NANOSECONDS)
  }
}
