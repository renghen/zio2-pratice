package com.rentech.zio2.http.json

import zio.*

import java.io.{BufferedReader, FileReader}
import java.net.URL

import scala.jdk.StreamConverters.*

import org.json4s.*
import org.json4s.native.JsonMethods.*
import org.json4s.JsonDSL.*

object Json101 extends ZIOAppDefault {
// Address(Awesome Stree,Super City)
  def run =
    for
      jsonFile <- ZIO.attempt(getClass.getResource("/netherlands.json"))
      str      <- readLines(jsonFile.getPath()).orDie
      _        <- Console.printLine(str)
    // _ <- Console.printLine(compact(render(res)) )
    yield ()

  def readLines(file: String): Task[String] = {
    def readLines(reader: BufferedReader): Task[String] =
      ZIO.attempt(reader.lines().toScala(Vector).mkString("\n"))

    def releaseReader(reader: BufferedReader): UIO[Unit] =
      ZIO.succeed(reader.close())

    def acquireReader(file: String): Task[BufferedReader] =
      ZIO.attempt(new BufferedReader(new FileReader(file), 2048))

    ZIO.acquireReleaseWith(acquireReader(file))(releaseReader)(readLines)
  }
}
