package com.jliermann.analyze.io

import java.io.File

import com.jliermann.analyze.RootConfiguration
import com.jliermann.analyze.environment.EnvironmentLive
import com.jliermann.utils.test.{PropTest, ResourcesTest}
import com.typesafe.config.ConfigFactory
import org.scalatest.TryValues

import scala.io.Codec

class FileIOLiveIT
  extends PropTest
    with ResourcesTest
    with TryValues {
  val config: RootConfiguration = RootConfiguration.loadConfigOrThrow(ConfigFactory.load())
  val rawFile = new File(getClass.getClassLoader.getResource(new File("testRawInput.txt").toString).getPath)

  "writeToFile and readFromFile" should "write seq of strings to a given file, then reread them" in forAll { content: Seq[String] =>
    withTmpFolder { folder =>
      val file = new File(folder, "file.txt")
      FileOutputLive.writeToFile(file, content)
      FileInputLive.readFromFile(file, Codec.UTF8).success.value should contain theSameElementsInOrderAs content
    }

  }

  "readEnregs" should "read and parse nested arrays into a matrix of signal" in {
    val expected = Seq(
      Seq(Seq(6.250000000000001E-4, 6.260823703105162E-4, 6.293288292626618E-4), Seq(0.004375, 0.016904223998383937, -0.011957247755990573)),
      Seq(Seq(-6.260823703105162E-4, 6.293288292626618E-4, 6.347374213109573E-4), Seq(0.008125000000000002, 0.00500865896248413, 0.0018879864877879853))
    )

    val result = FileInputLive.readEnregs(EnvironmentLive, config.localConfiguration.input.copy(file = rawFile))

    result.success.value shouldBe expected
  }

}
