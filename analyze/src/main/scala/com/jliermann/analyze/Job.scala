package com.jliermann.analyze

import com.jliermann.analyze.domain.SignalTypes.Signal
import com.jliermann.analyze.environment.JobEnvironment
import com.jliermann.analyze.seq.TrySeqOps._

import scala.util.{Failure, Success}

private[analyze] object Job {

  def run(env: JobEnvironment, config: RootConfiguration): Either[Seq[Throwable], Unit] = {
    import config._
    val result = for {
      input: Seq[Seq[Signal]] <- env.fileInput.readEnregs(env, localConfiguration.input)
      coefs = input.map(env.preparator.prepareEnreg(env, config.analyzeConfiguration))
      stringCoefs: Seq[String] = coefs
        .rights
        .map(_.mkString(";"))

      _ <- env.fileOutput.writeToFile(localConfiguration.output, stringCoefs)
    } yield coefs.lefts.flatten

    result match {
      case Success(Nil) => Right()
      case Success(exceptions) => Left(exceptions)
      case Failure(exception) => Left(exception :: Nil)
    }
  }

}
