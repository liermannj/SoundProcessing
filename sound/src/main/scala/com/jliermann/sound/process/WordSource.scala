package com.jliermann.sound.process

import akka.stream.IOResult
import akka.stream.scaladsl.Source
import com.jliermann.sound.SoundConfiguration
import com.jliermann.sound.domain.SpokenTag
import com.jliermann.sound.environment.WordSourceEnvironment

import scala.concurrent.Future

trait WordSource {

  val wordSource: WordSource.Service

}

object WordSource {

  trait Service {

    def source(env: WordSourceEnvironment, config: SoundConfiguration): Source[Seq[SpokenTag[Double]], Future[IOResult]]

  }
}