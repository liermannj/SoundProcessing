package com.jliermann.analyze.seq

import com.jliermann.analyze.seq.TrySeqOps._
import com.jliermann.utils.test.PropTest
import org.scalacheck.Arbitrary
import org.scalatest.TryValues

import scala.util.{Success, Try}

class TrySeqOpsSpec extends PropTest with TryValues {

  "mapSuccesses" should "apply a function to successful values in a seq" in forAll { seq: Seq[Try[Unit]] =>
    val result = RichTrySeq(seq).mapSuccesses(_ => 1)

    val countExpected = seq.count(_.isSuccess)
    val countResult = result.count {
      case Success(1) => true
      case _ => false
    }

    countResult shouldBe countExpected
    result should have length seq.length
  }

  "flatMapSuccesses" should "apply a failable function to successful values in a seq, then flatten it" in forAll { seq: Seq[Try[Unit]] =>
    val result = RichTrySeq(seq).mapSuccesses(_ => Arbitrary.arbitrary[Try[Unit]].sample.getOrElse(Success()))

    val countExpected = seq.count(_.isSuccess)
    val countResult = result.count(_.isSuccess)

    countResult should be <= countExpected
    result should have length seq.length
  }

  "squash" should "return an error if the initial seq contains any" in forAll { seq: Seq[Try[Unit]] =>
    val result = RichTrySeq(seq).squash(new Exception("my fancy exception"))

    seq.find(_.isFailure) match {
      case Some(_) => result.failure.exception shouldBe a[CompoundError]
      case None => result should be a 'success
    }
  }

  "successes and failures" should "be shortcuts to collect respectively Success and Failures" in forAll { seq: Seq[Try[Unit]] =>
    val successes = RichTrySeq(seq).successes
    val failures = RichTrySeq(seq).failures

    seq should have length (successes.length + failures.length)
  }

}