package com.jliermann.analyze

import com.jliermann.analyze.environment.JobEnvironment
import com.jliermann.analyze.seq.TrySeqOps._

import scala.util.Try

private[analyze] object Job {

  def run(env: JobEnvironment, config: RootConfiguration): Try[Unit] = {
    import config._

    val enregs = env.fileInput.readEnregs(env, localConfiguration.input)
      .flatMapSuccesses(env.preparator.prepareEnreg(env, config.analyzeConfiguration))
      .mapSuccesses(_.mkString(";"))

    for {
      _ <- env.fileOutput.writeToFile(localConfiguration.output, enregs.successes)
      _ <- enregs.squash(new Exception("Something went wrong"))
    } yield ()
  }

}
