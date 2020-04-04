package com.jliermann.utils.test

import java.io.File
import java.util.UUID

import resource._

import scala.io.{Codec, Source}

trait ResourcesTest {

  def readFile(file: File, codec: Codec = Codec.UTF8): Seq[String] = {
    managed(Source.fromFile(file.getPath)(codec)).acquireAndGet(_.getLines.to[Seq])
  }

  def readResource(file: File, codec: Codec = Codec.UTF8): Seq[String] = {
    managed(Source.fromFile(getClass.getClassLoader.getResource(file.toString).getPath)(codec)).acquireAndGet(_.getLines.to[Seq])
  }

  def withTmpFolder(test: File => Any): Any = {
    val tmpFolder = new File(UUID.randomUUID().toString)
    try {
      tmpFolder.mkdir()
      test(tmpFolder)
    } finally recursiveDelete(tmpFolder)
  }

  def recursiveDelete(f: File): Unit = {
    if(f.isDirectory) f.listFiles().foreach(recursiveDelete)
    f.delete()
  }

}
