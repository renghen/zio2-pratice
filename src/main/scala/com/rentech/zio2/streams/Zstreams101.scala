package com.rentech.zio2.streams

import zio.stream.{ZSink, ZStream}
import zio.{Console, ZIO, ZIOAppDefault}

import Console.*

object ZStreams101 extends ZIOAppDefault:
  def simpleStream =
    val stream = ZStream(1, 2, 3)
    val sink   = ZSink.foldLeft[Int, Int](0)((acc, in) => acc + in)
    val result = stream run sink
    result

  end simpleStream

  def run = printLine("ZStreams101...") *> simpleStream.flatMap(printLine(_))

end ZStreams101
