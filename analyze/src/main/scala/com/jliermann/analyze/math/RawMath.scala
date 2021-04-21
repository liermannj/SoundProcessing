package com.jliermann.analyze.math

trait RawMath {

  val rawMath: RawMath.Service

}

object RawMath {

  trait Service {

    def log(n: Double): Double

    def triangular(l: Int): Int => Double
  }

}
