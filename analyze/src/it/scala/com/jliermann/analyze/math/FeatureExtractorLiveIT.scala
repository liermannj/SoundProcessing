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

    val expected = FourierCoefs(4,
      Seq(0.0, 0.0, 0.0, 0.0, 684.3398473805472,
        0.0, 0.0, 0.0, 0.0, 180.62763730407823,
        0.0, 0.0, 0.0, 0.0, 287.76520483087296,
        0.0, 0.0, 0.0, 0.0, 30.085086079578918,
        0.0, 0.0, 0.0, 0.0, 452.3218559986062,
        0.0, 0.0, 0.0, 0.0, 129.79839182528352,
        0.0, 0.0, 0.0, 0.0, 23.860074187206134,
        0.0, 0.0, 0.0, 0.0, 13.321622732390168,
        0.0, 0.0, 0.0, 0.0, 8.961000034075722,
        0.0, 0.0, 0.0, 0.0, 6.533935034432711,
        0.0, 0.0, 0.0, 0.0, 4.993041379974666,
        0.0, 0.0, 0.0, 0.0, 3.934215442047746,
        0.0, 0.0, 0.0, 0.0, 3.165942687964123,
        0.0, 0.0, 0.0, 0.0, 2.585314306319867,
        0.0, 0.0, 0.0, 0.0, 2.132038848183569,
        0.0, 0.0, 0.0, 0.0, 1.7684554560021788,
        0.0, 0.0, 0.0, 0.0, 1.469829471479224,
        0.0, 0.0, 0.0, 0.0, 1.2192336703989377,
        0.0, 0.0, 0.0, 0.0, 1.0046672777835302,
        0.0, 0.0, 0.0, 0.0, 0.817346398282138,
        0.0, 0.0, 0.0, 0.0, 0.6506429488709826,
        0.0, 0.0, 0.0, 0.0, 0.49939909461829257,
        0.0, 0.0, 0.0, 0.0, 0.35946687799466875,
        0.0, 0.0, 0.0, 0.0, 0.22738624650870443,
        0.0, 0.0, 0.0, 0.0, 0.10014896298819873,
        0.0, 0.0, 0.0, 0.0, 0.03746724508385541,
        0.0, 0.0, 0.0, 0.0, 0.150642159804986,
        0.0, 0.0, 0.0, 0.0, 0.2794766354191903,
        0.0, 0.0, 0.0, 0.0, 0.4143161646383211,
        0.0, 0.0, 0.0, 0.0, 0.5583253309864258,
        0.0, 0.0, 0.0, 0.0, 0.7152071134988637,
        0.0, 0.0, 0.0, 0.0, 0.8894684858749009,
        0.0, 0.0, 0.0, 0.0, 1.0867899728988666,
        0.0, 0.0, 0.0, 0.0, 1.3145629986459737,
        0.0, 0.0, 0.0, 0.0, 1.5827025462811088,
        0.0, 0.0, 0.0, 0.0, 1.904924834286521,
        0.0, 0.0, 0.0, 0.0, 2.300841655000859,
        0.0, 0.0, 0.0, 0.0, 2.799561082278227,
        0.0, 0.0, 0.0, 0.0, 3.446241971510716,
        0.0, 0.0, 0.0, 0.0, 4.314905656189072,
        0.0, 0.0, 0.0, 0.0, 5.5359042387558635,
        0.0, 0.0, 0.0, 0.0, 7.362763223979862,
        0.0, 0.0, 0.0, 0.0, 10.368446836936046,
        0.0, 0.0, 0.0, 0.0, 16.242025882784144,
        0.0, 0.0, 0.0, 0.0, 36.417688003404365,
        0.0, 0.0, 0.0, 0.0, 224.15369123717278,
        0.0, 0.0, 0.0, 0.0, 259.5995898273447,
        0.0, 0.0, 0.0, 0.0, 15.686259579689123,
        0.0, 0.0, 0.0, 0.0, 566.0229173979498,
        0.0, 0.0, 0.0, 0.0, 192.58314638991706,
        0.0, 0.0, 0.0, 0.0, 542.3595351851745, 0.0))

    val result = for {
      fourier <- SignalTransformLive.fourier(EnvironmentLive, input)
      r <- FeatureExtractorLive.fourierCoefs(EnvironmentLive, fourier)
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

    val expected = MFC(Seq(
      0.0, 0.0, 0.0, 0.0, 5951.946612908994,
      0.0, 0.0, 0.0, 0.0, 823.0772897023425,
      0.0, 0.0, 0.0, 0.0, 291.1431027572753,
      0.0, 0.0, 0.0, 0.0, 1818.3166159172058,
      0.0, 0.0, 0.0, 0.0, 538.7275650290146,
      0.0, 0.0, 0.0, 0.0, -1567.4424994496744,
      0.0, 0.0, 0.0, 0.0, -927.2424551205779,
      0.0, 0.0, 0.0, 0.0, 798.4799502716876,
      0.0, 0.0, 0.0, 0.0, 65.6507883457806,
      0.0, 0.0, 0.0, 0.0, -2831.3487796513145,
      0.0, 0.0, 0.0, 0.0, -4472.940798027662,
      0.0, 0.0, 0.0, 0.0, -3129.925707893254,
      0.0, 0.0, 0.0, 0.0, -925.5535855844012,
      0.0, 0.0, 0.0, 0.0, -1108.723862508293,
      0.0, 0.0, 0.0, 0.0, -3200.3459619397095,
      0.0, 0.0, 0.0, 0.0, -3274.6076745615305,
      0.0, 0.0, 0.0, 0.0, -1273.9356908186576,
      0.0, 0.0, 0.0, 0.0, -1757.0102443153435,
      0.0, 0.0, 0.0, 0.0, -4216.292575377993,
      0.0, 0.0, 0.0, 0.0, -2167.34633410834,
      0.0, 0.0, 0.0, 0.0, 4701.934354658239,
      0.0, 0.0, 0.0, 0.0, 6137.749314212413,
      0.0, 0.0, 0.0, 0.0, 2726.859693517058,
      0.0, 0.0, 0.0, 0.0, 1743.8683993939787,
      0.0, 0.0, 0.0, 0.0, 3487.3996725166385,
      0.0, 0.0, 0.0, 0.0, 3143.6118114978995,
      0.0, 0.0, 0.0, 0.0, 889.5038325413867,
      0.0, 0.0, 0.0, 0.0, 670.5114945596811,
      0.0, 0.0, 0.0, 0.0, 2723.8094599838764,
      0.0, 0.0, 0.0, 0.0, 3471.7933430291437,
      0.0, 0.0, 0.0, 0.0, 1228.1581080794472,
      0.0, 0.0, 0.0, 0.0, -1629.1838307123255,
      0.0, 0.0, 0.0, 0.0, -2042.4519944170358,
      0.0, 0.0, 0.0, 0.0, -218.36077051761276,
      0.0, 0.0, 0.0, 0.0, 338.1111381822091,
      0.0, 0.0, 0.0, 0.0, -1842.152812681674,
      0.0, 0.0, 0.0, 0.0, -3104.970196246547,
      0.0, 0.0, 0.0, 0.0, -1632.7702900203662,
      0.0, 0.0, 0.0, 0.0, -2005.8676757676085,
      0.0, 0.0, 0.0, 0.0, -6252.214187998497,
      0.0, 0.0, 0.0, 0.0, -7671.159832827223,
      0.0, 0.0, 0.0, 0.0, -1755.333951250755,
      0.0, 0.0, 0.0, 0.0, 2083.2753604877676,
      0.0, 0.0, 0.0, 0.0, 671.237619819417,
      0.0, 0.0, 0.0, 0.0, -211.08371225912205,
      0.0, 0.0, 0.0, 0.0, 1859.9310428843582,
      0.0, 0.0, 0.0, 0.0, 2814.9468842560855,
      0.0, 0.0, 0.0, 0.0, 1087.7768009412166,
      0.0, 0.0, 0.0, 0.0, 180.16966442114523,
      0.0, 0.0, 0.0, 0.0, 2183.2852221757735,
      0.0, 0.0, 0.0, 0.0, 2872.4760866036568,
      0.0, 0.0))

    val result = for {
      fourier <- SignalTransformLive.fourier(EnvironmentLive, input)
      coefs <- FeatureExtractorLive.fourierCoefs(EnvironmentLive, fourier)
      r <- FeatureExtractorLive.mfc(EnvironmentLive, coefs)
    } yield r

    result.success.value shouldBe expected
  }

}
