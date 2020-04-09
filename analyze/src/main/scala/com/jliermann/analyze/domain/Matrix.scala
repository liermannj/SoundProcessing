package com.jliermann.analyze.domain

case class Matrix[T](xxs: Seq[Seq[T]]) {

  lazy val rows: Seq[Seq[T]] = xxs

  lazy val cols: Seq[Seq[T]] = transpose.xxs

  def mapCase[U](f: T => U): Matrix[U] = mapRows(_.map(f))

  def mapRows[U](f: Seq[T] => Seq[U]): Matrix[U] = Matrix(rows.map(f))

  def mapCols[U](f: Seq[T] => Seq[U]): Matrix[U] = Matrix(cols.map(f)).transpose

  def transpose: Matrix[T] = {
    @scala.annotation.tailrec
    def transposeRec(input: Seq[Seq[T]], acc: Seq[Seq[T]] = Nil): Seq[Seq[T]] = {
      if(input.headOption.isEmpty || input.head.isEmpty) acc
      else transposeRec(input.map(_.tail), acc :+ input.map(_.head))
    }

    Matrix(transposeRec(xxs))
  }
}