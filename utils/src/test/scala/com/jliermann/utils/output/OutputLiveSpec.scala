package com.jliermann.utils.output

import java.io.File

import akka.stream.scaladsl.Source
import com.jliermann.utils.test.{PropTest, ResourcesTest, StreamTest}
import org.scalacheck.{Arbitrary, Gen}

import scala.concurrent.Await
import scala.concurrent.duration._

class OutputLiveSpec extends StreamTest with ResourcesTest with PropTest {

  "sinkToFile" should "write data to the given file, separated by carriage return" in
    forAll(Gen.resize(100, Arbitrary.arbitrary[List[String]])) { sourceSeq: List[String] =>
      withTmpFolder { folder =>
        val tmpFile = new File(folder, "tmp.txt")
        tmpFile.createNewFile()

        Await.result(Source(sourceSeq).runWith(OutputLive.sinkToFile(tmpFile)), 10.second)

        val result = readFile(tmpFile)
        result should contain theSameElementsInOrderAs sourceSeq
      }
  }

}
