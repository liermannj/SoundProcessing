package com.jliermann.analyze.seq

import scala.util.{Failure, Success, Try}

object TrySeqOps {

  implicit class RichTrySeq[T](val trySeq: Seq[Try[T]]) extends AnyVal {

    def successes: Seq[T] = trySeq.collect { case Success(value) => value }

    def failures: Seq[Throwable] = trySeq.collect { case Failure(e) => e }
  }

  implicit class RichEitherSeq[T, U](val trySeq: Seq[Either[U, T]]) extends AnyVal {

    def rights: Seq[T] = trySeq.collect { case Right(value) => value }

    def lefts: Seq[U] = trySeq.collect { case Left(value) => value }
  }

}
