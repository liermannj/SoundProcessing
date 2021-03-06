package com.jliermann.sound.process

import akka.stream.IOResult
import akka.stream.scaladsl.Source
import com.jliermann.sound.SoundConfiguration
import com.jliermann.sound.domain.SpokenTag
import com.jliermann.sound.environment.WordSourceEnvironment
import javax.sound.sampled.TargetDataLine

import scala.concurrent.Future

object WordSourceLive extends WordSourceLive
trait WordSourceLive extends WordSource.Service {
  override def source(env: WordSourceEnvironment, config: SoundConfiguration, tdl: TargetDataLine): Source[Seq[SpokenTag[Double]], Future[IOResult]] = {
    env.audioInput.audioWave(env, config.audioFormat, tdl)
      .via(env.audioWindowing.window(env, config.samplingRate, config.overlapping))
      .via(env.audioWindowing.labelPitched(config.limitPitchedFrame))
      .via(env.audioWindowing.keepCoherentChunk(env, config.silentSeparator))
  }
}