package com.jliermann.analyze.seq

import org.apache.commons.math3.util.{FastMath => fm}

object SeqOps {

  implicit class RichSeq[T](val xs: Seq[T]) extends AnyVal {

    def safeCenteredSlice(middle: Int, halfInterval: Int): Seq[T] = safeSlice(middle - halfInterval, middle + halfInterval)

    def safeSlice(from: Int, to: Int): Seq[T] = xs.slice(fm.max(0, from), fm.min(to, xs.length - 1))

  }

}
