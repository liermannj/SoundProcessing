package com.jliermann.sound.domain

import com.jliermann.utils.test.PropSpec

class SpokenTagSpec extends PropSpec with FrameGens {

  "label" should "flag as pitched or silent dependent on a given limit" in forAll { (frame: Frame[Double], limit: Double) =>
    SpokenTag.label(limit)(frame) match {
      case Pitched(xs) => xs shouldBe frame.xs
      case Silent => succeed
    }
  }

  it should "label every frame as Pitched when limit is <= 0" in forAll { (frames: Seq[Frame[Double]], limit: Double) =>
    val trueLimit = - math.abs(limit)
    val result = frames.map(SpokenTag.label(trueLimit))
    result.foreach {
      case Silent => fail()
      case _ =>
    }

    succeed
  }
}
