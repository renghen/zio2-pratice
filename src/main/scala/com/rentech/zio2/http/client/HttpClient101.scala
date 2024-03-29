package com.rentech.zio2.http.client

import zio.http.Client

import zio.*

object SimpleClient extends ZIOAppDefault:
  val url = "https://opensky-network.org/api/states/all"

  val program = for
    res  <- Client.request(url)
    data <- res.body.asString
  // _    <- Console.printLine(data)
  yield ()

  override val run = program.provide(Client.default)

end SimpleClient
