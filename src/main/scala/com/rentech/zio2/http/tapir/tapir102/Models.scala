package com.rentech.zio2.http.tapir.tapir102

import sttp.client3.*
import sttp.client3.httpclient.zio.{send, HttpClientZioBackend}

import sttp.tapir.client.sttp.SttpClientInterpreter
import sttp.tapir.client.sttp.ws.zio.*
import sttp.tapir.endpoint
import sttp.tapir.given

import zio.http.Status.NotFound

import zio.{ZIO, ZLayer}

object Models:
  private val rAll = HttpClientZioBackend().flatMap { backend =>
    val request =
      basicRequest.get(uri"https://opensky-network.org/api/states/all").response(asString)
    backend.send(request)
  }

//   val backend    = HttpClientZioBackend()
//   val requestAll = basicRequest
//     .get(uri"https://httpbin.org/post?hello=world")
//     .response(asString)

  def getAll(): ZIO[Any, Throwable, String] = // send(requestAll).provide(ZLayer.fromZIO(backend))
    for
      resEither <- rAll
      body      <- ZIO.fromEither(resEither.body).mapError(err => Exception("could not get data"))
    yield body

end Models
