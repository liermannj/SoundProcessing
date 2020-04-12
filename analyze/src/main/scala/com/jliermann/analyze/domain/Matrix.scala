package com.jliermann.analyze.domain

import scala.util.{Failure, Success, Try}

case class Matrix[T] private(xxs: Seq[Seq[T]]) {

  def transpose: Matrix[T] = {
    @scala.annotation.tailrec
    def transposeRec(input: Seq[Seq[T]], acc: Seq[Seq[T]] = Nil): Seq[Seq[T]] = {
      if (input.headOption.isEmpty || input.head.isEmpty) acc
      else transposeRec(input.map(_.tail), acc :+ input.map(_.head))
    }

    new Matrix(transposeRec(xxs))
  }
}

object Matrix {

  def apply[T](xxs: Seq[Seq[T]]): Try[Matrix[T]] = {
    if (xxs.map(_.length).distinct.length <= 1) Success(new Matrix(xxs))
    else Failure(new IllegalArgumentException(s"Matrix must be rectangular, found $xxs"))
  }
}