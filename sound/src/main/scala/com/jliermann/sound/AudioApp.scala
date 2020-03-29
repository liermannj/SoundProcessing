package com.jliermann.sound

import javax.sound.sampled.{AudioFormat, AudioSystem, TargetDataLine}
import resource._

trait AudioApp {

  def getTargetDataLine(fmt: AudioFormat): ManagedResource[TargetDataLine] = {
    def start: TargetDataLine = {
      val tdl = AudioSystem.getTargetDataLine(fmt)
      tdl.open(fmt)
      tdl.start()
      tdl
    }

    def stop(targetDataLine: TargetDataLine): Unit = {
      targetDataLine.stop()
      targetDataLine.close()
    }
    makeManagedResource(start)(stop)(Nil)
  }

}
