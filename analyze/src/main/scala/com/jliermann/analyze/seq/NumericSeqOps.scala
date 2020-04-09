package com.jliermann.analyze.seq

import org.apache.commons.math3.util.FastMath

object NumericSeqOps {

  implicit class RichNumericSeq[T](val xs: Seq[T]) extends AnyVal {

    def mean(implicit num: Numeric[T]) : Double = num.toDouble(xs.sum) / xs.length

    def stdDev(implicit num: Numeric[T]): Double = FastMath.sqrt {
      xs.map(num.toDouble).map(a => math.pow(a - mean, 2)).sum / xs.size
    }

  }

}
