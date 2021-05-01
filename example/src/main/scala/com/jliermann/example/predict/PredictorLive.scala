package com.jliermann.example.predict
import akka.NotUsed
import akka.stream.scaladsl.Flow
import com.jliermann.analyze.domain.SignalTypes.Coefs
import com.jliermann.example.PredictorConfig
import sys.process._

object PredictorLive extends PredictorLive
trait PredictorLive extends Predictor.Service{
  override def predict(predictor: PredictorConfig): Flow[Coefs, String, NotUsed] = {
    Flow[Coefs]
      .map(_.mkString(predictor.featureSep))
      .map(features => s"python ${predictor.script} ${predictor.file} $features".!!)
  }
}