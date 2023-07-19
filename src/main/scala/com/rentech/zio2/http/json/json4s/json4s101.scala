package com.rentech.zio2.http.json.json4s

import org.json4s.*
import org.json4s.native.Serialization.{read, write}

import com.rentech.zio2.http.json.State

//   val state: List[String | Int] = List(
//     "aa441c",
//     "UAL20   ",
//     "United States",
//     0
//   )

object json4s101 extends App {
  implicit val formats: Formats = DefaultFormats + new StateSerializer
// val str = write[Address](Address("Awesome Stree", "Super City"))
// {"Street":"Awesome Stree","City":"Super City"}
  val str                       =
    """["aa441c","UAL20   ", "United States",1687245490, 1687245490, 4.7712,52.31,null,
    true, 0, 182.81, null, null, null,"6253",false,0]"""

  val state = read[State](str)
  println(state)
  println(write(state))
}
