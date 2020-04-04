package com.jliermann.analyze.transform

import com.jliermann.analyze.AnalyzeConfiguration
import com.jliermann.analyze.domain.SignalTypes.{Coefs, Signal}
import com.jliermann.analyze.environment.PreparatorEnv

import scala.util.Try

trait Preparator {

  val preparator: Preparator.Service

}

object Preparator {

  trait Service {

    def prepareSignal(env: PreparatorEnv, signal: Signal, config: AnalyzeConfiguration): Try[Coefs]

  }
}
