package com.rentech.zio2

import zio.*
import zio.Console.*
import java.io.IOException

object MyApp extends ZIOAppDefault:

  def run: ZIO[Environment & ZIOAppArgs & Scope, Any, Any] = myAppLogic

  val myAppLogic: Task[Unit] =
    for
      _ <- printLine("Hello! What is your name?")
      name <- readLine
      _ <- printLine(s"Hello, ${name}, welcome to ZIO!")
    yield ()

end MyApp
