package com.jliermann.analyze.io

import com.jliermann.analyze.seq.TrySeqOps._
import com.jliermann.utils.test.PropTest

class FileInputLiveSpec extends PropTest {

  "tryToDouble" should "read string array as double array" in forAll { (seq: Seq[Double], sep: Char) =>

    FileInputLive.tryToDouble(sep.toString)(seq.mkString(sep.toString)).successes should contain theSameElementsInOrderAs seq

  }

}
