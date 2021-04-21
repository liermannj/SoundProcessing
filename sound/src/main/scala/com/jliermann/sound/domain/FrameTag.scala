package com.jliermann.sound.domain

case class Frame[T](xs: Seq[T])

sealed trait SpokenTag[+T] {

  def isSilent: Boolean

  def isPitched: Boolean

}

case class Pitched[+T](xs: Seq[T]) extends SpokenTag[T] {

  def isSilent: Boolean = false

  def isPitched: Boolean = true

}

case object Silent extends SpokenTag[Nothing] {

  def isSilent: Boolean = true

  def isPitched: Boolean = false

}

object SpokenTag {

  def label(floor: Double)(frame: Frame[Double]): SpokenTag[Double] = {
    if (frame.xs.map(Math.pow(_, 2)).sum >= floor) Pitched(frame.xs)
    else Silent
  }

}