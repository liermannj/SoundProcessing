package com.jliermann.analyze.math

import com.jliermann.analyze.domain.SignalTypes.{FourierCoefs, MFC}
import com.jliermann.analyze.environment.EnvironmentLive
import org.apache.commons.math3.util.{FastMath => fm}
import org.scalatest.{FlatSpec, Matchers, TryValues}

class FeatureExtractorLiveIT
  extends FlatSpec
    with Matchers
    with TryValues {

  type Power = Double
  type Frequency = Double

  "fourierCoefs" should "extract the first fourier coeficients from a fourier transform using triangular aggregation" in {
    val inputFrequencies: Seq[(Power, Frequency)] = Seq(
      5D -> 4D,
      4D -> 12D,
      3D -> 24D,
      1D -> 28D,
      // noise
      0.1D -> 256D,
      0.01 -> 1024D
    )
    val inputFct = (x: Double) => inputFrequencies.map { case (power, freq) => power * fm.cos(2 * fm.PI * freq * x) }.sum
    val input = (0 to 255).map(_ / 255D).map(inputFct)

    val expected = FourierCoefs(Seq(
      684.3398473805472, //   4
      43.26023924687131, //   8   ==> empty
      566.0229173979485, //   12
      16.644655791867603, //  16  ==> empty
      44.726421098032745, //  20  ==> empty
      452.3218559986062, //   24
      157.41359361055635, // 28
      36.417688003404336)) // 32 ==> empty

    val result = for {
      fourier <- SignalTransformLive.fourier(EnvironmentLive, input)
      r <- FeatureExtractorLive.fourierCoefs(EnvironmentLive, fourier, 8)
    } yield r

    result.success.value shouldBe expected
  }

  "mfc" should "extract the first mfc coeficients from fourier coefs" in {
    val inputFrequencies: Seq[(Power, Frequency)] = Seq(
      5D -> 4D,
      4D -> 12D,
      3D -> 24D,
      1D -> 28D,
      // noise
      0.1D -> 256D,
      0.01 -> 1024D
    )
    val inputFct = (x: Double) => inputFrequencies.map { case (power, freq) => power * fm.cos(2 * fm.PI * freq * x) }.sum
    val input = (0 to 255).map(_ / 255D).map(inputFct)

    val expected = MFC(Seq(-13.298223378637829, 572.1987842702171, -442.35841447409655, -424.8348924042035, 642.1925490279787))

    val result = for {
      fourier <- SignalTransformLive.fourier(EnvironmentLive, input)
      coefs <- FeatureExtractorLive.fourierCoefs(EnvironmentLive, fourier, 8)
      r <- FeatureExtractorLive.mfc(EnvironmentLive, coefs, 5)
    } yield r

    result.success.value shouldBe expected
  }

}
