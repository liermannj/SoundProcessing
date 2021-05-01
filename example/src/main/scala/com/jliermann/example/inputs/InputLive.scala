package com.jliermann.example.inputs
import akka.NotUsed
import akka.stream.IOResult
import akka.stream.scaladsl.Source
import com.jliermann.analyze.domain.SignalTypes.Coefs
import com.jliermann.example.FeatureConfiguration
import com.jliermann.example.domain.Category
import com.jliermann.example.environment.{FeatureEnvironment, InputEnvironment}
import com.jliermann.sound.domain.Pitched

import javax.sound.sampled.TargetDataLine
import scala.concurrent.Future
import scala.util.Success

object InputLive extends InputLive
trait InputLive extends Input.Service {
  override def categories: Source[Category, NotUsed] = {
    Source
      .repeat( () => util.Random.nextInt(Category.elements.size))
      .map(i => Category.elements(i()))
      .zipWithIndex
      .map{case (category, index) => println(index); println(category.label); category}
  }

  // TODO : add finer error handling
  def features(env: FeatureEnvironment, config: FeatureConfiguration, tdl: TargetDataLine): Source[Coefs, Future[IOResult]] = {
    env.wordSource.source(env, config.soundConfiguration, tdl)
      .map(_.collect { case Pitched(xs) => xs})
      .map(env.preparator.prepareEnreg(env, config.analyzeConfiguration))
      .collect { case Success(value) => value}
  }

  override def trainingFixture(env: InputEnvironment, config: FeatureConfiguration, tdl: TargetDataLine): Source[(Category, Coefs), NotUsed] = {
    env.input.categories
      .zip(env.input.features(env, config, tdl))
  }
}
