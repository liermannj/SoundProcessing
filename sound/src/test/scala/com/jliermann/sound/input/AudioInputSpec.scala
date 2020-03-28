package com.jliermann.sound.input

import com.jliermann.utils.test.{PropTest, StreamTest}
import javax.sound.sampled.AudioFormat
import org.scalacheck.Gen

import scala.concurrent.duration._

class AudioInputSpec extends StreamTest with PropTest {

  "audioWave" should "read input stream, process via unpack, then flatten the whole" in {
    val audioFormat = new AudioFormat(8000F, 8, 1, false, false)

    val input = AudioInputEnvMock.rawAudioSource.audioInputStream(null).readAllBytes()
    val expected = awaitResult(10.seconds)(AudioInputLive.audioWave(AudioInputEnvMock, audioFormat)).map(_.toByte)

    input should contain theSameElementsInOrderAs expected
  }

  "normalBytesFromBits" should "be the euclidean division by 8 for all positive values" in
    forAll(Gen.posNum[Int]) { number: Int =>
      AudioInputLive.normalBytesFromBits(number) shouldBe (number / 8)
    }

}
