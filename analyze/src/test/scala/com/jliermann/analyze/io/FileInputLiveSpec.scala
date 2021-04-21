package com.jliermann.analyze.io

import com.jliermann.utils.test.PropTest
import org.scalatest.TryValues

class FileInputLiveSpec extends PropTest with TryValues {

  "tryToDouble" should "read string array as double array" in forAll { (seq: Seq[Double], sep: Char) =>
    whenever(seq.nonEmpty) {
      FileInputLive.tryToSignal(sep.toString)(seq.mkString(sep.toString)).success.value should contain theSameElementsInOrderAs seq
    }
  }

}
