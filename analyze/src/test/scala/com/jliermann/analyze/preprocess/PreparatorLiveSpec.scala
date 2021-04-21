package com.jliermann.analyze.preprocess

import java.util.concurrent.atomic.AtomicInteger

import com.jliermann.analyze.domain.SignalTypes
import com.jliermann.analyze.domain.SignalTypes.{Enreg, Signal}
import com.jliermann.analyze.environment._
import com.jliermann.analyze.math.{FeatureExtractor, FeatureExtractorLive}
import com.jliermann.analyze.{FeatureExtractionConfig, RootConfiguration}
import com.jliermann.utils.test.PropTest
import com.typesafe.config.ConfigFactory
import org.scalatest.TryValues

import scala.util.{Failure, Success, Try}

class PreparatorLiveSpec extends PropTest with TryValues {

  import PreparatorLiveSpec._

  "prepareSignal" should "compute different features for a signal" in forAll { signal: Signal =>
    val fourierCount = new AtomicInteger(0)
    val mfcCount = new AtomicInteger(0)
    val result = PreparatorLive.prepareSignal(JobEnvironmentMock(fourierCount, mfcCount), rootConfig.analyzeConfiguration)(signal)

    result match {
      case Success(_) =>
        fourierCount.get() shouldBe 1
        mfcCount.get() shouldBe 1
      case Failure(_) => succeed
    }
  }

  "prepareEnreg" should "prepare inner signals then aggregate by mean and std" in forAll { signals: Enreg =>
    val fourierCount = new AtomicInteger(0)
    val mfcCount = new AtomicInteger(0)

    PreparatorLive.prepareEnreg(JobEnvironmentMock(fourierCount, mfcCount), rootConfig.analyzeConfiguration)(signals) match {
      case Success(result) =>
        fourierCount.get() shouldBe signals.length
        mfcCount.get() shouldBe signals.length
        val numberOfFeature = PreparatorLive.prepareSignal(EnvironmentLive, rootConfig.analyzeConfiguration)(signals.head).success.value.length
        result should have length numberOfFeature * 2
      case Failure(_) => succeed
    }
  }

}

object PreparatorLiveSpec {
  val rootConfig: RootConfiguration = RootConfiguration.loadConfigOrThrow(ConfigFactory.load())

  object FeatureExtractorMock {
    def apply(fourierCount: AtomicInteger, mfcCount: AtomicInteger): FeatureExtractor.Service = new FeatureExtractorLive {
      override def fourierCoefs(env: FourierFeatureExtractorEnv, fourier: SignalTypes.Fourier, config: FeatureExtractionConfig): Try[SignalTypes.FourierCoefs] = {
        fourierCount.incrementAndGet()
        super.fourierCoefs(env, fourier, config)
      }

      override def mfc(env: MFCFeatureExtractorEnv, fourierCoefs: SignalTypes.FourierCoefs, config: FeatureExtractionConfig): Try[SignalTypes.MFC] = {
        mfcCount.incrementAndGet()
        super.mfc(env, fourierCoefs, config)
      }
    }
  }

  object JobEnvironmentMock {
    def apply(fourierCount: AtomicInteger, mfcCount: AtomicInteger): JobEnvironment = new EnvironmentLive {
      override val featureExtractor: FeatureExtractor.Service = FeatureExtractorMock(fourierCount, mfcCount)
    }
  }

}
