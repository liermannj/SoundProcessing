package com.jliermann.sound.domain

object FFT {
  class Complex(val re: Double, val im: Double) {
    def +(rhs: Complex) = new Complex(re + rhs.re, im + rhs.im)

    def -(rhs: Complex) = new Complex(re - rhs.re, im - rhs.im)

    def *(rhs: Complex) = new Complex(re * rhs.re - im * rhs.im, rhs.re * im + re * rhs.im)

    def magnitude: Double = Math.hypot(re, im)

    def phase: Double = Math.atan2(im, re)

    override def toString = s"Complex($re, $im)"
  }

  def fft(x: Array[Complex]): Array[Complex] = {
    require(x.length > 0 && (x.length & (x.length - 1)) == 0, "array size should be power of two")
    fft(x, 0, x.length, 1)
  }

  def fft(x: Array[Double]): Array[Complex] = fft(x.map(re => new Complex(re, 0.0)))

  def rfft(x: Array[Double]): Array[Complex] = fft(x).take(x.length / 2 + 1)

  private def fft(x: Array[Complex], start: Int, n: Int, stride: Int): Array[Complex] = {
    if (n == 1)
      return Array(x(start))

    val X = fft(x, start, n / 2, 2 * stride) ++ fft(x, start + stride, n / 2, 2 * stride)

    for (k <- 0 until n / 2) {
      val t = X(k)
      val arg = -2 * math.Pi * k / n
      val c = new Complex(math.cos(arg), math.sin(arg)) * X(k + n / 2)
      X(k) = t + c
      X(k + n / 2) = t - c
    }
    X
  }
}
