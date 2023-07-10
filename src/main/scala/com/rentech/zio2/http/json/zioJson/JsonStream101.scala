package com.rentech.zio2.http.json.zioJson

import zio.*
import zio.Console.*
import zio.json.*
import zio.stream.*
import States.{given}
import com.rentech.zio2.http.json.State
import java.io.IOException
import java.nio.charset.Charset

object JsonStream101 extends ZIOAppDefault:

  val streamStatePipeline = {
    val stream = ZStream
      .fromResource("netherlands.json")
      .via(ZPipeline.decodeCharsWith(Charset.defaultCharset()))
      .via(
        JsonDecoder[List[State]].decodeJsonPipeline(JsonStreamDelimiter.Newline)
      )
      .flatMap(ZStream.fromIterable)

    stream.runCollect.map(s => s.toList)
  }

  val streamStateNormal = {
    val stream =
      ZStream
        .fromResource("netherlands.json")
        .via(ZPipeline.utf8Decode)
        .flatMap { s =>
          ZStream.fromIterable(s.toCharArray())
        }
    JsonDecoder[List[State]].decodeJsonStream(stream)
  }

  def run =
    for
      _ <- printLine("streams...")
      states <- streamStatePipeline
      _ <- printLine(s"states count = ${states.size}")
    yield ExitCode.success
