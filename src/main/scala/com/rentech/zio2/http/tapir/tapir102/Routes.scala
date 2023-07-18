package com.rentech.zio2.http.tapir.tapir102

import sttp.tapir.server.metrics.prometheus.PrometheusMetrics
import sttp.tapir.swagger.bundle.SwaggerInterpreter
import sttp.tapir.ztapir.ZServerEndpoint
import sttp.tapir.{endpoint, query, stringBody, PublicEndpoint}
import sttp.tapir.given

import zio.{Task, ZIO}

object Routes:
  val prometheusMetrics: PrometheusMetrics[Task] = PrometheusMetrics.default[Task]()

  val helloEndpoint: PublicEndpoint[String, Unit, String, Any] = endpoint
    .get
    .in("hello")
    .in(query[String]("name"))
    .out(stringBody)

  val allEndpoint: PublicEndpoint[Unit, Unit, String, Any] = endpoint
    .get
    .in("all")
    .out(stringBody)

  val helloServerEndpoint: ZServerEndpoint[Any, Any] =
    helloEndpoint.serverLogicSuccess(user => ZIO.succeed(s"Hello ${user}"))

  val allServerEndpoint: ZServerEndpoint[Any, Any] =
    allEndpoint.serverLogicSuccess{_ => Models.getAll() }

  val docEndpoints: List[ZServerEndpoint[Any, Any]] = SwaggerInterpreter()
    .fromServerEndpoints[Task](List(helloServerEndpoint, allServerEndpoint), "tapir101", "1.0.0")

  val all = List(helloServerEndpoint, allServerEndpoint) ++ docEndpoints
