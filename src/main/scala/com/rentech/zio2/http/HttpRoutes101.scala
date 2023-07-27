package com.rentech.zio2.http

import zio.http.*

import zio.*

object HttpRoutes101 extends ZIOAppDefault:
  val app = Http.collect[Request] {
    case Method.GET -> Root / "fruits" / "a" => Response.text("Apple")
    case Method.GET -> Root / "fruits" / "b" => Response.text("Banana")
  }

  override def run = Server.serve(app).provide(Server.defaultWithPort(8090))
