package com.rentech.zio2.http.tapir

package com.softwaremill

import sttp.tapir.server.ziohttp.{ZioHttpInterpreter, ZioHttpServerOptions}
import sttp.tapir.server.metrics.prometheus.PrometheusMetrics
import sttp.tapir.ztapir.ZServerEndpoint
import sttp.tapir.*

import zio.{Console, LogLevel, Scope, Task, ZIO, ZIOAppArgs, ZIOAppDefault, ZLayer}
import zio.http.{HttpApp, Server}
import zio.logging.LogFormat
import zio.logging.backend.SLF4J

object Endpoints:
  val prometheusMetrics: PrometheusMetrics[Task]               = PrometheusMetrics.default[Task]()
  val helloEndpoint: PublicEndpoint[String, Unit, String, Any] = endpoint
    .get
    .in("hello")
    .in(query[String]("name"))
    .out(stringBody)
  val helloServerEndpoint: ZServerEndpoint[Any, Any]           =
    helloEndpoint.serverLogicSuccess(user => ZIO.succeed(s"Hello ${user}"))

  val all = List(helloServerEndpoint)
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
        _          <- Console.printLine(
          s"Go to http://localhost:${actualPort}/docs to open SwaggerUI. Press ENTER key to exit.",
        )
        _          <- Console.readLine
      yield ()
    ).provide(
      Server.defaultWithPort(port),
    ).exitCode
