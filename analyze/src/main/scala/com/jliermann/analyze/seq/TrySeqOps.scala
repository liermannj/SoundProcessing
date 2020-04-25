package com.jliermann.analyze.seq

import com.jliermann.analyze.seq.TrySeqOps.CompoundError._
import com.jliermann.analyze.{EOL, PAD}

import scala.util.{Failure, Success, Try}

object TrySeqOps {

  implicit class RichTrySeq[T](val trySeq: Seq[Try[T]]) extends AnyVal {

    def mapSuccesses[U](f: T => U): Seq[Try[U]] = flatMapSuccesses(f.andThen(Success(_)))

    def flatMapSuccesses[U](f: T => Try[U]): Seq[Try[U]] = trySeq.map {
      case Success(v) => f(v)
      case Failure(e) => Failure(e)
    }

    def squash(err: Throwable): Try[Seq[T]] = trySeq.failures match {
      case Nil => Success(trySeq.successes)
      case errors => Failure(CompoundError(err, errors))
    }

    def successes: Seq[T] = trySeq.collect { case Success(value) => value }

    def failures: Seq[Throwable] = trySeq.collect { case Failure(e) => e }
  }

  case class CompoundError(err: Throwable, broadStack: Seq[Throwable]) extends Exception(s"${err.getMessage}$EOL${stackErrors(broadStack)}", err)

  object CompoundError {

    private def stackErrors(errors: Seq[Throwable]): String = errors
      .map(err => s"$err$EOL${err.getStackTrace.mkString(s"${PAD}at ", s"$EOL${PAD}at ", "")}")
      .map(_.replaceAll(s"$EOL", s"$EOL$PAD|"))
      .mkString(s"${PAD}Contains : ", s"$EOL${PAD}Contains : ", "")

  }

}
