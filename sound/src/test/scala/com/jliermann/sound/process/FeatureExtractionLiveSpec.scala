package com.jliermann.sound.process

import com.jliermann.sound.domain.{FrameGens, MatrixGen, Pitched, Silent, SpokenTag}
import com.jliermann.utils.test.{PropTest, StreamTest}
import MatrixGen._
import akka.NotUsed
import akka.stream.scaladsl.Source
import org.scalacheck.{Arbitrary, Gen}
import org.scalatest.TryValues
import scala.concurrent.duration._

import scala.util.{Success, Try}

class FeatureExtractionLiveSpec
  extends PropTest
    with StreamTest
    with FrameGens
    with TryValues {

  "intensity" should "return the absolute maximum of a frame if pitched" in forAll { frame: Pitched[Double] =>
    whenever(frame.xs.length > 1) {
      FeatureExtractionLive.intensity(frame).success.value should be > 0.0
    }
  }

  it should "fail if the pitched frame is empty" in {
    val input = Pitched(Seq.empty[Double])
    FeatureExtractionLive.intensity(input).failure.exception shouldBe an[UnsupportedOperationException]
  }

  it should "return 0 if the frame is silent" in {
    FeatureExtractionLive.intensity(Silent).success.value shouldBe 0.0
  }

  "fft" should "return the magnitude of a signal if the frame is pitched (meaning non null transform)" in forAll { frame: Pitched[Double] =>
    whenever(frame.xs.length > 2) {
      val transformed = FeatureExtractionLive.fft(13, frame)
      transformed.success.value.distinct.length should be > 0
    }
  }

  it should "fail if it is not at least of the minimum power of 2 size" in forAll(Gen.posNum[Int]) { features: Int =>
    val input0 = Pitched(Seq.empty[Double])
    val input1 = Pitched(Seq(1.0))
    FeatureExtractionLive.fft(features, input0).failure.exception shouldBe an[IllegalArgumentException]
  }

  it should "return the number of features asked in parameters" in forAll(Gen.posNum[Int], Arbitrary.arbitrary[SpokenTag[Double]]) {
    (features: Int, frame: SpokenTag[Double]) =>
    whenever(frame.isSilent || (frame.isPitched && frame.asInstanceOf[Pitched[Double]].xs.length > 2)) {
      FeatureExtractionLive.fft(features, frame).success.value should have length features
    }
  }

  it should "return a vector of zeros if spokenTag is silent" in forAll(Gen.posNum[Int]) { features: Int =>
    FeatureExtractionLive.fft(features, Silent).success.value.distinct shouldBe Seq(0.0)
  }

  "meansAndStds" should "return two informations for each feature" in forAll { matrix: MatrixGen =>
    FeatureExtractionLive.meansAndStds(matrix.xxs).success.value should have length matrix.xxs.head.length * 2
  }

  it should "fail if the array is not a matrix" in forAll { notAMatrix: Seq[Seq[Double]] =>
    whenever(notAMatrix.map(_.length).distinct.length > 1) {
      FeatureExtractionLive.meansAndStds(notAMatrix).failure.exception shouldBe an[IllegalArgumentException]
    }
  }

  it should "success on empty matrix" in {
    val input = Seq.empty[Seq[Double]]
    FeatureExtractionLive.meansAndStds(input) should be a 'success
  }

  it should "success on empty matrix columns" in forAll(Gen.choose(1, 50)) { l: Int =>
    val input = Seq.fill(l)(Seq.empty[Double])
    FeatureExtractionLive.meansAndStds(input) should be a 'success
  }

  "features" should "convert raw audio enreg into elaborated features" in {
    val expected = Seq(1.0, 2.0)
    object FeatureExtractionMock extends FeatureExtractionLive {
      override def intensity(frame: SpokenTag[Double]): Try[Double] = Success(1.0)

      override def fft(fftFeatures: Int, frame: SpokenTag[Double]): Try[Seq[Double]] = Success(Seq(1.0, 2.0))

      override def meansAndStds(features: Seq[Seq[Double]]): Try[Seq[Double]] = Success(expected)
    }

    object MockEnv extends FeatureExtraction {
      override val featureExtraction: FeatureExtraction.Service = FeatureExtractionMock
    }

    val source = arbSource[Seq[SpokenTag[Double]]](100)
    val result = awaitResult(10.second)(source.via(FeatureExtractionLive.features(MockEnv, 13)))

    result.foreach(_ shouldBe Seq(1.0, 2.0))
  }
}
