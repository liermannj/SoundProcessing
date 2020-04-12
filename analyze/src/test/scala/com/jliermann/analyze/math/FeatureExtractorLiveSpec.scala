package com.jliermann.analyze.math

import com.jliermann.analyze.FeatureExtractionConfig
import com.jliermann.analyze.domain.SignalTypeSpec._
import com.jliermann.analyze.domain.SignalTypes._
import com.jliermann.analyze.environment.{AggregateEnv, TransformatorEnv}
import com.jliermann.utils.test.PropTest
import org.scalatest.TryValues

import scala.util.{Failure, Success, Try}

class FeatureExtractorLiveSpec extends PropTest with TryValues {

  import FeatureExtractorLiveSpec._

  "fourierCoefs" should "extract the fundamental frequency of the Fourier spectrum" in forAll { fourier: Fourier =>

    val result = FeatureExtractorLive.fourierCoefs(MockEnv, fourier, defaultConfig).success.value.freq.toInt
    val expected = FeatureExtractorMock.fourierFundamental(fourier).success.value

    result shouldBe expected
  }

  it should "be consistent over two executions" in forAll { fourier: Fourier =>

    val result1 = FeatureExtractorLive.fourierCoefs(MockEnv, fourier, defaultConfig)
    val result2 = FeatureExtractorLive.fourierCoefs(MockEnv, fourier, defaultConfig)

    result1 match {
      case Failure(e) => e should have message result2.failure.exception.getMessage
      case Success(result) => result shouldBe result2.success.value
    }
  }

  "mfc" should "be consistent over two executions" in forAll { fourierCoefs: FourierCoefs =>

    val result1 = FeatureExtractorLive.mfc(MockEnv, fourierCoefs, defaultConfig)
    val result2 = FeatureExtractorLive.mfc(MockEnv, fourierCoefs, defaultConfig)

    result1 match {
      case Failure(e) => e should have message result2.failure.exception.getMessage
      case Success(result) => result shouldBe result2.success.value
    }
  }

  it should "not consider the first two elements of the cosine transformed" in { c: Cosine =>
    object FixedCosineMock extends SignalTransformMock {
      override def cosine(env: TransformatorEnv, signal: Signal): Try[Cosine] = Success(c)
    }

    object FixedCosineMockEnv extends SignalTransform with FeatureExtractor with RawMath {
      override val signalTransform: SignalTransform.Service = FixedCosineMock
      override val featureExtractor: FeatureExtractor.Service = FeatureExtractorMock
      override val rawMath: RawMath.Service = RawMathLive
    }
    val result = FeatureExtractorLive.mfc(FixedCosineMockEnv, null, defaultConfig)
    result.success.value.coefs.drop(2) should contain theSameElementsInOrderAs c.xs.reverse.drop(2).reverse

  }
}

object FeatureExtractorLiveSpec {

  val defaultConfig: FeatureExtractionConfig = FeatureExtractionConfig(Int.MaxValue, None)

  trait SignalTransformMock extends SignalTransformLive {
    override def aggregateWindow(env: AggregateEnv)(seq: Signal): Try[Coef] = Success(seq.sum)

    override def normalize(seq: Signal): Signal = seq
  }

  trait FeatureExtractorMock extends FeatureExtractorLive {
    override def fourierFundamental(fourier: Fourier): Try[Int] = Success(4)
  }

  object SignalTransformMock extends SignalTransformMock

  object FeatureExtractorMock extends FeatureExtractorMock

  object MockEnv extends SignalTransform with FeatureExtractor with RawMath {
    override val signalTransform: SignalTransform.Service = SignalTransformMock
    override val featureExtractor: FeatureExtractor.Service = FeatureExtractorMock
    override val rawMath: RawMath.Service = RawMathLive
  }

}
