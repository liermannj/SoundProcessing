package com.jliermann.analyze.preprocess

import com.jliermann.analyze.AnalyzeConfiguration
import com.jliermann.analyze.domain.SignalTypes.{Coefs, Enreg, Signal}
import com.jliermann.analyze.environment.{EnregPreparatorEnvironment, PreparatorEnv}

import scala.util.Try

trait Preparator {

  val preparator: Preparator.Service

}

object Preparator {

  trait Service {
    def prepareEnreg(env: EnregPreparatorEnvironment, config: AnalyzeConfiguration)(enregs: Enreg): Try[Coefs]

    def prepareSignal(env: PreparatorEnv, config: AnalyzeConfiguration)(signal: Signal): Try[Coefs]

  }

}
