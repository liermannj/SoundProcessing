package com.jliermann.analyze.preprocess

import com.jliermann.analyze.AnalyzeConfiguration
import com.jliermann.analyze.domain.SignalTypes.{Coefs, Signal}
import com.jliermann.analyze.environment.{EnregPreparatorEnv, PreparatorEnv}

import scala.util.Try

trait Preparator {

  val preparator: Preparator.Service

}

object Preparator {

  trait Service {
    def prepareEnreg(env: EnregPreparatorEnv, config: AnalyzeConfiguration)(enregs: Seq[Signal]): Either[Seq[Throwable], Coefs]

    def prepareSignal(env: PreparatorEnv, config: AnalyzeConfiguration)(signal: Signal): Try[Coefs]

  }

}