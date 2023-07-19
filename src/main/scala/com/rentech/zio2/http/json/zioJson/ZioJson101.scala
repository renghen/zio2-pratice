package com.rentech.zio2.http.json.zioJson

import zio.*
import zio.json.*

import com.rentech.zio2.http.json.State

import States.given

object ZioJson101 extends ZIOAppDefault:
  val str =
    """[["aa441c","UAL20   ", "United States",1687245490, 1687245490, 4.7712,52.31,null,
    true, 0, 182.81, null, null, null,"6253",false,0]]"""
  def run =
    for
      state <- ZIO.fromEither(str.fromJson[Vector[State]])
      _     <- Console.printLine("decoding sucessful...")
      _     <- Console.printLine(s"state: $state")
    yield ExitCode.success
