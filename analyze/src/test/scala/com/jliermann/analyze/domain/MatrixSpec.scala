package com.jliermann.analyze.domain

import com.jliermann.utils.test.PropTest
import org.scalacheck.{Arbitrary, Gen}
import org.scalatest.TryValues

class MatrixSpec extends PropTest {

  import MatrixSpec._

  "transpose" should "de the identity operation if done twice on square matrix" in forAll { m: Matrix[Int] =>
    m
      .transpose
      .transpose
      .xxs.zip(m.xxs)
      .foreach { case (l, r) => l should contain theSameElementsInOrderAs r }
  }

  it should "have a inverted width and height" in forAll { m: Matrix[Int] =>
    val transposed = m.transpose
    val newHeight = transposed.xxs.length
    val newWidth = transposed.xxs.headOption.map(_.length).getOrElse(0)

    val height = m.xxs.length
    val width = m.xxs.headOption.map(_.length).getOrElse(0)

    newHeight shouldBe width
    newWidth shouldBe height
  }
}

object MatrixSpec extends TryValues {

  implicit val arbMatrix: Arbitrary[Matrix[Int]] = Arbitrary {
    for {
      n <- Gen.posNum[Int]
      s <- Gen.listOf(Gen.listOfN(n, Arbitrary.arbitrary[Int]))
    } yield Matrix(s).success.value
  }

}