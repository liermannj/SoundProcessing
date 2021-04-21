package com.jliermann.analyze.math

import com.jliermann.analyze.domain.SignalTypes.Signal
import com.jliermann.utils.test.PropTest
import org.scalatest.TryValues

class SignalTransformLiveSpec extends PropTest with TryValues {

  import com.jliermann.analyze.math.SignalTransformLiveSpec._

  "positiveShift" should "add a constant to a seq so its minimum becomes 0" in forAll { signal: Signal =>
    val result = SignalTransformLive.positiveShift(signal)
    if (signal.nonEmpty) result.min should be >= 0D
    else result shouldBe empty
  }

  "normalize" should "divide a signal so its power is comprised between -1 and 1" in forAll { signal: Signal =>
    val result = SignalTransformLive.normalize(signal)
    if (signal.nonEmpty) {
      result.min should be >= -1D
      result.max should be <= 1D
    }
    else result shouldBe empty
  }

  "aggregateWindow" should "apply a variable coef on a signal then sum it" in forAll { (signal: Signal, coef: Double) =>
    val result = SignalTransformLive.aggregateWindow(RawMathMockEnv(coef))(signal)
    val expected = signal.sum * coef

    result.success.value should equal(expected +- 0.00001)
  }

  "fillSignal" should "complete the length of a signal to the next power of 2 with zeros" in forAll { signal: Signal =>
    val result = SignalTransformLive.fillSignal(signal).success.value
    result.length should be >= signal.length
    result.length % 2 shouldBe 0
    result.sum shouldBe signal.sum
  }
}

object SignalTransformLiveSpec {

  object RawMathMock {
    def apply(coef: Double): RawMath.Service = new RawMathLive {
      override def triangular(l: Int): Int => Double = _ => coef
    }
  }

  object RawMathMockEnv {
    def apply(coef: Double): RawMath = new RawMath {
      override val rawMath: RawMath.Service = RawMathMock(coef)
    }
  }

}
