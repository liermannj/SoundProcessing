package com.jliermann.example.domain

import resource._

sealed trait Category {
  def label: String
}

sealed abstract class StaticCategory(override val label: String) extends Category

object Category {

  case object Up extends StaticCategory("Up")
  case object Down extends StaticCategory("Down")
  case object Left extends StaticCategory("Left")
  case object Right extends StaticCategory("Right")

  case object Trash extends Category {
    // TODO : inject path
    private val dict = managed(scala.io.Source.fromResource("english_dict.txt")).acquireAndGet(_.getLines.toSet)
    override def label: String = {
      val n = util.Random.nextInt(dict.size)
      dict.iterator.drop(n).next
    }
  }

  val elements: Seq[Category] = Up :: Down :: Left :: Right :: Trash :: Nil
}