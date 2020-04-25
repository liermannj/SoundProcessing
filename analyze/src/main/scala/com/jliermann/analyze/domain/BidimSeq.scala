package com.jliermann.analyze.domain

trait BidimSeq[T] {
  val lines: Seq[Seq[T]]

  def mapCase[U](f: T => U): BidimSeq[U] = mapLine(_.map(f))

  def mapLine[U](f: Seq[T] => Seq[U]): BidimSeq[U] = BidimSeq(lines.map(f))
}

object BidimSeq {

  def apply[T](xxs: Seq[Seq[T]]): BidimSeq[T] = new BidimSeq[T] {
    override val lines: Seq[Seq[T]] = xxs
  }
}