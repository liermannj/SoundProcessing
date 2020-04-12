package com.jliermann.analyze.math

import org.apache.commons.math3.util.{FastMath => fm}

object RawMathLive extends RawMathLive

trait RawMathLive extends RawMath.Service {
  override def log(n: Double): Double = {
    val logN = fm.log(n)
    if (logN.isInfinity || logN.isNaN) 0 else logN
  }

  override def triangular(l: Int): Int => Double = (x: Int) => -fm.abs(-1 + 2 * (x.toDouble / l)) + 1
}
