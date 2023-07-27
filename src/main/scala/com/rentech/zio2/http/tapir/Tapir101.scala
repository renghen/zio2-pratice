package com.rentech.zio2.http.tapir

import sttp.tapir.*
import sttp.tapir.server.metrics.prometheus.PrometheusMetrics
import sttp.tapir.server.ziohttp.{ZioHttpInterpreter, ZioHttpServerOptions}
import sttp.tapir.swagger.bundle.SwaggerInterpreter
import sttp.tapir.ztapir.ZServerEndpoint

import zio.logging.LogFormat
import zio.logging.backend.SLF4J

import zio.http.{HttpApp, Server}

import zio.{Console, LogLevel, Scope, Task, ZIO, ZIOAppArgs, ZIOAppDefault, ZLayer}

import Console.*

object Endpoints:
  val prometheusMetrics: PrometheusMetrics[Task] = PrometheusMetrics.default[Task]()

  val helloEndpoint: PublicEndpoint[String, Unit, String, Any] = endpoint
    .get
    .in("hello")
    .in(query[String]("name"))
    .out(stringBody)

  val helloEndpoint2: PublicEndpoint[String, Unit, String, Any] = endpoint
    .get
    .in("hello2")
    .in(query[String]("name"))
    .out(stringBody)

  val helloServerEndpoint: ZServerEndpoint[Any, Any] =
    helloEndpoint.serverLogicSuccess(user => ZIO.succeed(s"Hello ${user}"))

  val helloServerEndpoint2: ZServerEndpoint[Any, Any] =
    helloEndpoint2.serverLogicSuccess(user => ZIO.succeed(s"Hello ${user.toUpperCase()}"))

  val docEndpoints: List[ZServerEndpoint[Any, Any]] = SwaggerInterpreter()
    .fromServerEndpoints[Task](List(helloServerEndpoint, helloServerEndpoint2), "tapir101", "1.0.0")

  val all = List(helloServerEndpoint, helloServerEndpoint2) ++ docEndpoints
end Endpoints

object Main extends ZIOAppDefault:
  override val bootstrap: ZLayer[ZIOAppArgs, Any, Environment] =
    SLF4J.slf4j(LogLevel.Debug, LogFormat.default)

  override def run: ZIO[ZIOAppArgs with Scope, Any, Any] =
    val serverOptions: ZioHttpServerOptions[Any] =
      ZioHttpServerOptions
        .customiseInterceptors
        .metricsInterceptor(Endpoints.prometheusMetrics.metricsInterceptor())
        .options

    val app: HttpApp[Any, Throwable] = ZioHttpInterpreter(serverOptions).toHttp(Endpoints.all)

    val port = sys.env.get("HTTP_PORT").flatMap(_.toIntOption).getOrElse(8080)

    (
      for
        actualPort <- Server.install(app.withDefaultErrorResponse)
        _          <- printLine(
          s"Go to http://localhost:${actualPort}/docs to open SwaggerUI. Press ENTER key to exit.",
        )
        _          <- readLine
      yield ()
    ).provide(
      Server.defaultWithPort(port),
    ).exitCode
