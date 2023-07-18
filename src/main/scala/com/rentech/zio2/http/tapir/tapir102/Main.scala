package com.rentech.zio2.http.tapir.tapir102

import sttp.tapir.server.ziohttp.{ZioHttpInterpreter, ZioHttpServerOptions}
import zio.{Console, LogLevel, Scope, Task, ZIO, ZIOAppArgs, ZIOAppDefault, ZLayer}
import zio.http.{HttpApp, Server}
import zio.logging.LogFormat
import zio.logging.backend.SLF4J

object Main extends ZIOAppDefault:
  override val bootstrap: ZLayer[ZIOAppArgs, Any, Environment] =
    SLF4J.slf4j(LogLevel.Debug, LogFormat.default)

  override def run: ZIO[ZIOAppArgs with Scope, Any, Any] =
    import Console.*

    val serverOptions: ZioHttpServerOptions[Any] =
      ZioHttpServerOptions
        .customiseInterceptors
        .metricsInterceptor(Routes.prometheusMetrics.metricsInterceptor())
        .options

    val app  = ZioHttpInterpreter(serverOptions).toHttp(Routes.all)
    val port = sys.env.get("HTTP_PORT").flatMap(_.toIntOption).getOrElse(8080)
    {
      for
        actualPort <- Server.install(app.withDefaultErrorResponse)
        consoleInfo =
          s"Go to http://localhost:${actualPort}/docs to open SwaggerUI. Press ENTER key to exit."
        _ <- printLine(consoleInfo)
        _ <- readLine
      yield ()
    }.provide(
      Server.defaultWithPort(port),
    ).exitCode
