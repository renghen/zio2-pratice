package com.rentech.zio2.http

import zio.*
import zio.http.*

object Http101 extends ZIOAppDefault:
  val app = Handler.text("Hello World from ZIO-http 3").toHttp

  override def run = Server.serve(app).provide(Server.defaultWithPort(8090))
