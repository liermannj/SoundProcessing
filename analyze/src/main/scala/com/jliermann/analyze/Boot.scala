package com.jliermann.analyze

import com.jliermann.analyze.domain.SignalTypes.{FourierCoefs, MFC}
import com.jliermann.analyze.environment.EnvironmentLive
import org.apache.commons.math3.transform.{DftNormalization, FastFourierTransformer}
import org.apache.commons.math3.util.FastMath

object Boot extends App {
  import A._

  val out = for {
    fourier <- EnvironmentLive.signalTransform.fourier(EnvironmentLive, input)
    fc @ FourierCoefs(firstFreq, fourierCoefs) <- EnvironmentLive.featureExtractor.fourierCoefs(EnvironmentLive, fourier, 20)
    MFC(mfcc) <- EnvironmentLive.featureExtractor.mfc(EnvironmentLive, fc, 12)
    result = firstFreq +: (fourierCoefs ++ mfcc)
  } yield {
    println(result.mkString(";"))
  }

  out.get

}

object A {
  val input = Seq(1.348738117,
  0.98078528,
  0.603386945,
  0.423879533,
  0.528367874,
  0.831469612,
  1.126563844,
  1.207106781,
  0.987946675,
  0.555570233,
  0.117843346,
  -0.117316568,
  -0.063268713,
  0.195090322,
  0.451570531,
  0.5,
  0.25553625,
  -0.195090322,
  -0.643838068,
  -0.882683432,
  -0.824950127,
  -0.555570233,
  -0.280839894,
  -0.207106781,
  -0.419457063,
  -0.831469612,
  -1.235474655,
  -1.423879533,
  -1.310493726,
  -0.98078528,
  -0.641631336,
  -0.5,
  -0.641631336,
  -0.98078528,
  -1.310493726,
  -1.423879533,
  -1.235474655,
  -0.831469612,
  -0.419457063,
  -0.207106781,
  -0.280839894,
  -0.555570233,
  -0.824950127,
  -0.882683432,
  -0.643838068,
  -0.195090322,
  0.25553625,
  0.5,
  0.451570531,
  0.195090322,
  -0.063268713,
  -0.117316568,
  0.117843346,
  0.555570233,
  0.987946675,
  1.207106781,
  1.126563844,
  0.831469612,
  0.528367874,
  0.423879533,
  0.603386945,
  0.98078528,
  1.348738117,
  1.5,
  1.348738117,
  0.98078528,
  0.603386945,
  0.423879533,
  0.528367874,
  0.831469612,
  1.126563844,
  1.207106781,
  0.987946675,
  0.555570233,
  0.117843346,
  -0.117316568,
  -0.063268713,
  0.195090322,
  0.451570531,
  0.5,
  0.25553625,
  -0.195090322,
  -0.643838068,
  -0.882683432,
  -0.824950127,
  -0.555570233,
  -0.280839894,
  -0.207106781,
  -0.419457063,
  -0.831469612,
  -1.235474655,
  -1.423879533,
  -1.310493726,
  -0.98078528,
  -0.641631336,
  -0.5,
  -0.641631336,
  -0.98078528,
  -1.310493726,
  -1.423879533,
  -1.235474655,
  -0.831469612,
  -0.419457063,
  -0.207106781,
  -0.280839894,
  -0.555570233,
  -0.824950127,
  -0.882683432,
  -0.643838068,
  -0.195090322,
  0.25553625,
  0.5,
  0.451570531,
  0.195090322,
  -0.063268713,
  -0.117316568,
  0.117843346,
  0.555570233,
  0.987946675,
  1.207106781,
  1.126563844,
  0.831469612,
  0.528367874,
  0.423879533,
  0.603386945,
  0.98078528,
  1.348738117,
  1.5,
  1.348738117,
  0.98078528,
  0.603386945,
  0.423879533,
  0.528367874,
  0.831469612,
  1.126563844,
  1.207106781,
  0.987946675,
  0.555570233,
  0.117843346,
  -0.117316568,
  -0.063268713,
  0.195090322,
  0.451570531,
  0.5,
  0.25553625,
  -0.195090322,
  -0.643838068,
  -0.882683432,
  -0.824950127,
  -0.555570233,
  -0.280839894,
  -0.207106781,
  -0.419457063,
  -0.831469612,
  -1.235474655,
  -1.423879533,
  -1.310493726,
  -0.98078528,
  -0.641631336,
  -0.5,
  -0.641631336,
  -0.98078528,
  -1.310493726,
  -1.423879533,
  -1.235474655,
  -0.831469612,
  -0.419457063,
  -0.207106781,
  -0.280839894,
  -0.555570233,
  -0.824950127,
  -0.882683432,
  -0.643838068,
  -0.195090322,
  0.25553625,
  0.5,
  0.451570531,
  0.195090322,
  -0.063268713,
  -0.117316568,
  0.117843346,
  0.555570233,
  0.987946675,
  1.207106781,
  1.126563844,
  0.831469612,
  0.528367874,
  0.423879533,
  0.603386945,
  0.98078528,
  1.348738117,
  1.5,
  1.348738117,
  0.98078528,
  0.603386945,
  0.423879533,
  0.528367874,
  0.831469612,
  1.126563844,
  1.207106781,
  0.987946675,
  0.555570233,
  0.117843346,
  -0.117316568,
  -0.063268713,
  0.195090322,
  0.451570531,
  0.5,
  0.25553625,
  -0.195090322,
  -0.643838068,
  -0.882683432,
  -0.824950127,
  -0.555570233,
  -0.280839894,
  -0.207106781,
  -0.419457063,
  -0.831469612,
  -1.235474655,
  -1.423879533,
  -1.310493726,
  -0.98078528,
  -0.641631336,
  -0.5,
  -0.641631336,
  -0.98078528,
  -1.310493726,
  -1.423879533,
  -1.235474655,
  -0.831469612,
  -0.419457063,
  -0.207106781,
  -0.280839894,
  -0.555570233,
  -0.824950127,
  -0.882683432,
  -0.643838068,
  -0.195090322,
  0.25553625,
  0.5,
  0.451570531,
  0.195090322,
  -0.063268713,
  -0.117316568,
  0.117843346,
  0.555570233,
  0.987946675,
  1.207106781,
  1.126563844,
  0.831469612,
  0.528367874,
  0.423879533,
  0.603386945,
  0.98078528,
  1.348738117,
  1.5,
  1.348738117,
  0.98078528,
  0.603386945,
  0.423879533,
  0.528367874,
  0.831469612,
  1.126563844,
  1.207106781,
  0.987946675,
  0.555570233,
  0.117843346,
  -0.117316568,
  -0.063268713,
  0.195090322,
  0.451570531,
  0.5,
  0.25553625,
  -0.195090322,
  -0.643838068,
  -0.882683432,
  -0.824950127,
  -0.555570233,
  -0.280839894,
  -0.207106781,
  -0.419457063,
  -0.831469612,
  -1.235474655,
  -1.423879533,
  -1.310493726,
  -0.98078528,
  -0.641631336,
  -0.5,
  -0.641631336,
  -0.98078528,
  -1.310493726,
  -1.423879533,
  -1.235474655,
  -0.831469612,
  -0.419457063,
  -0.207106781,
  -0.280839894,
  -0.555570233,
  -0.824950127,
  -0.882683432,
  -0.643838068,
  -0.195090322,
  0.25553625,
  0.5,
  0.451570531,
  0.195090322,
  -0.063268713,
  -0.117316568,
  0.117843346,
  0.555570233,
  0.987946675,
  1.207106781,
  1.126563844,
  0.831469612,
  0.528367874,
  0.423879533,
  0.603386945,
  0.98078528,
  1.348738117,
  1.5,
  1.348738117,
  0.98078528,
  0.603386945,
  0.423879533,
  0.528367874,
  0.831469612,
  1.126563844,
  1.207106781,
  0.987946675,
  0.555570233,
  0.117843346,
  -0.117316568,
  -0.063268713,
  0.195090322,
  0.451570531,
  0.5,
  0.25553625,
  -0.195090322,
  -0.643838068,
  -0.882683432,
  -0.824950127,
  -0.555570233,
  -0.280839894,
  -0.207106781,
  -0.419457063,
  -0.831469612,
  -1.235474655,
  -1.423879533,
  -1.310493726,
  -0.98078528,
  -0.641631336,
  -0.5,
  -0.641631336,
  -0.98078528,
  -1.310493726,
  -1.423879533,
  -1.235474655,
  -0.831469612,
  -0.419457063,
  -0.207106781,
  -0.280839894,
  -0.555570233,
  -0.824950127,
  -0.882683432,
  -0.643838068,
  -0.195090322,
  0.25553625,
  0.5,
  0.451570531,
  0.195090322,
  -0.063268713,
  -0.117316568,
  0.117843346,
  0.555570233,
  0.987946675,
  1.207106781,
  1.126563844,
  0.831469612,
  0.528367874,
  0.423879533,
  0.603386945,
  0.98078528,
  1.348738117,
  1.5,
  1.348738117,
  0.98078528,
  0.603386945,
  0.423879533,
  0.528367874,
  0.831469612,
  1.126563844,
  1.207106781,
  0.987946675,
  0.555570233,
  0.117843346,
  -0.117316568,
  -0.063268713,
  0.195090322,
  0.451570531,
  0.5,
  0.25553625,
  -0.195090322,
  -0.643838068,
  -0.882683432,
  -0.824950127,
  -0.555570233,
  -0.280839894,
  -0.207106781,
  -0.419457063,
  -0.831469612,
  -1.235474655,
  -1.423879533,
  -1.310493726,
  -0.98078528,
  -0.641631336,
  -0.5,
  -0.641631336,
  -0.98078528,
  -1.310493726,
  -1.423879533,
  -1.235474655,
  -0.831469612,
  -0.419457063,
  -0.207106781,
  -0.280839894,
  -0.555570233,
  -0.824950127,
  -0.882683432,
  -0.643838068,
  -0.195090322,
  0.25553625,
  0.5,
  0.451570531,
  0.195090322,
  -0.063268713,
  -0.117316568,
  0.117843346,
  0.555570233,
  0.987946675,
  1.207106781,
  1.126563844,
  0.831469612,
  0.528367874,
  0.423879533,
  0.603386945,
  0.98078528,
  1.348738117,
  1.5,
  1.348738117,
  0.98078528,
  0.603386945,
  0.423879533,
  0.528367874,
  0.831469612,
  1.126563844,
  1.207106781,
  0.987946675,
  0.555570233,
  0.117843346,
  -0.117316568,
  -0.063268713,
  0.195090322,
  0.451570531,
  0.5,
  0.25553625,
  -0.195090322,
  -0.643838068,
  -0.882683432,
  -0.824950127,
  -0.555570233,
  -0.280839894,
  -0.207106781,
  -0.419457063,
  -0.831469612,
  -1.235474655,
  -1.423879533,
  -1.310493726,
  -0.98078528,
  -0.641631336,
  -0.5,
  -0.641631336,
  -0.98078528,
  -1.310493726,
  -1.423879533,
  -1.235474655,
  -0.831469612,
  -0.419457063,
  -0.207106781,
  -0.280839894,
  -0.555570233,
  -0.824950127,
  -0.882683432,
  -0.643838068,
  -0.195090322,
  0.25553625,
  0.5,
  0.451570531,
  0.195090322,
  -0.063268713,
  -0.117316568,
  0.117843346,
  0.555570233,
  0.987946675,
  1.207106781,
  1.126563844,
  0.831469612,
  0.528367874,
  0.423879533,
  0.603386945,
  0.98078528,
  1.348738117,
  1.5)


}