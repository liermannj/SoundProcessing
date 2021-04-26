package com.jliermann.example.inputs

import akka.NotUsed
import akka.stream.IOResult
import akka.stream.scaladsl.Source
import com.jliermann.analyze.domain.SignalTypes.Coefs
import com.jliermann.example.FeatureConfiguration
import com.jliermann.example.domain.Category
import com.jliermann.example.environment.{FeatureEnvironment, InputEnvironment}

import javax.sound.sampled.TargetDataLine
import scala.concurrent.Future

trait Input {

  val input: Input.Service

}

object Input {

  trait Service {

    def categories: Source[Category, NotUsed]

    def features(env: FeatureEnvironment, config: FeatureConfiguration, tdl: TargetDataLine): Source[Coefs, Future[IOResult]]

    def trainingFixture(env: InputEnvironment, config: FeatureConfiguration, tdl: TargetDataLine): Source[(Category, Coefs), NotUsed]
  }
}
