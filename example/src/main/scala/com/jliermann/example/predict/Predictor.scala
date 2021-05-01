package com.jliermann.example.predict

import akka.NotUsed
import akka.stream.scaladsl.Flow
import com.jliermann.analyze.domain.SignalTypes.Coefs
import com.jliermann.example.PredictorConfig

import java.io.File

trait Predictor {

  val predictor: Predictor.Service

}

object Predictor {

  trait Service {

    def predict(predictor: PredictorConfig): Flow[Coefs, String, NotUsed]

  }
}
