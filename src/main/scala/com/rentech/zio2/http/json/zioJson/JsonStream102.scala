package com.rentech.zio2.http.json.zioJson

import zio.*
import zio.Console.*
import zio.json.*
import zio.json.ast.*
import zio.stream.*
import States.given
import com.rentech.zio2.http.json.State

import java.io.IOException
import java.nio.charset.Charset
import com.rentech.zio2.http.json.json4s.json4s101.str
import zio.json.ast.Json.Arr
import com.rentech.zio2.http.json.json4s.json4s101.state

object JsonStream102 extends ZIOAppDefault:

  /*
  val streamStatePipeline = {
    val stream = ZStream
      .fromResource("all.json")
      .via(ZPipeline.decodeCharsWith(Charset.defaultCharset()))
      .via(
        JsonDecoder[List[State]].decodeJsonPipeline(JsonStreamDelimiter.Newline)
      )
      .flatMap(ZStream.fromIterable)

    stream.runCollect.map(s => s.toList)
  }
   */
  val streamStateNormal = {
    val stream =
      ZStream
        .fromResource("all.json")
        .via(ZPipeline.utf8Decode)
        .flatMap { s =>
          ZStream.fromIterable(s.toCharArray())
        }

    val cursor: JsonCursor[Json, Arr] = JsonCursor.field("states").isArray
    val jsonDecoder                   = JsonDecoder[Json].map(_.get(cursor))

    for {
      arr <- jsonDecoder.decodeJsonStream(stream = stream).absolve
      lst = arr.elements
      result <- ZIO.foreach(lst) { el =>
        ZIO.fromEither(JsonDecoder[State].fromJsonAST(el))
      }
    } yield result
  }

  val streamStateAdvance = {
    val stream =
      ZStream
        .fromResource("all.json")
        .via(ZPipeline.utf8Decode)
        .flatMap { s =>
          ZStream.fromIterable(s.toCharArray())
        }

    val cursor: JsonCursor[Json, Arr] = JsonCursor.field("states").isArray
    val jsonDecoder                   = JsonDecoder[Json].map(_.get(cursor))
    ZStream
      .fromZIO(jsonDecoder.decodeJsonStream(stream = stream).absolve)
      .flatMap(arr => ZStream.fromChunk(arr.elements))
      .mapZIO(json => ZIO.fromEither(JsonDecoder[State].fromJsonAST(json)))
      .runCollect
  }

  def run =
    for
      _      <- printLine("streams...")
      states <- streamStateAdvance
      _      <- printLine(s"states count = ${states.length}")
      keys = states.groupBy(_.originCountry).keys
      _ <- ZIO.foreach(keys)(printLine(_))
    yield ExitCode.success
