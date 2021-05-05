package com.jliermann.analyze

import com.jliermann.analyze.domain.SignalTypes.Coefs
import com.jliermann.analyze.environment.JobEnvironment
import com.jliermann.analyze.seq.TrySeqOps._

import scala.util.Try

private[analyze] object Job {

  def run(env: JobEnvironment, config: RootConfiguration): Try[Unit] = {
    import config._

    val enregs: Seq[Try[Seq[Coefs]]] = env.fileInput.readEnregs(env, localConfiguration.input)
      .flatMapSuccesses(env.preparator.prepareEnreg(env, config.analyzeConfiguration))

    for {
      _ <- env.fileOutput.writeToFile(localConfiguration.output, enregs.successes)
      _ <- enregs.squash(new Exception("Something went wrong"))
    } yield ()
  }

}
