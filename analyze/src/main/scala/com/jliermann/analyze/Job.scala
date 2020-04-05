package com.jliermann.analyze

import com.jliermann.analyze.domain.SignalTypes.{Coef, Coefs, Signal}
import com.jliermann.analyze.environment.JobEnvironment
import org.apache.commons.math3.util.FastMath
import scala.util.{Success, Try}

private[analyze] object Job {

  def run(env: JobEnvironment, config: RootConfiguration): Try[Unit] = {
    import config._
    for {
      input: Seq[Seq[Signal]] <- env.fileInput.readSignal(env, localConfiguration.input)
      transformed: Seq[Coefs] = input
        .map { in =>
          val tr = in.map(env.preparator.prepareSignal(env, _, analyzeConfiguration))
            .collect { case Success(value) => value }


          tr// transpose
            .foldLeft(tr.map(_ => Seq.empty[Double])) {
              case (transpose: Seq[Seq[Coef]], values) =>
                val zippedValues: Seq[(Coef, Int)] = values.zipWithIndex
                transpose
                  .zipWithIndex
                  .map{case (acc: Seq[Coef], i) => acc :+ zippedValues.find(_._2 == i).get._1}
            }// compute
            .flatMap {xs =>
            val mean = xs.sum / xs.length
            val std = xs.map(_ - mean).map(FastMath.pow(_, 2)).sum
            mean :: std :: Nil
          }
        }

      trToString = transformed.map(_.mkString(";"))
      _ <- env.fileOutput.writeToFile(localConfiguration.output, trToString)
    } yield ()
  }

}
