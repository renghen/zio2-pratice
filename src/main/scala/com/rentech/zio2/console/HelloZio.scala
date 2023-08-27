package com.rentech.zio2.console

import java.io.IOException

import zio.Console.*
import zio.*

object MyApp extends ZIOAppDefault:
  def run: ZIO[Environment & ZIOAppArgs & Scope, Any, Any] = myAppLogic

  val myAppLogic: Task[Unit] =
    for
      _    <- printLine("Hello! What is your name?")
      name <- readLine
      _    <- printLine(s"Hello, ${name}, welcome to ZIO!")
    yield ()

end MyApp
