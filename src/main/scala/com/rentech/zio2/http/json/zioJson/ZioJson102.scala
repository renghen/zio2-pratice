package com.rentech.zio2.http.json.zioJson

import zio.json.*

final case class FooAction(action: String)

object FooAction:
  given JsonDecoder[FooAction] = DeriveJsonDecoder.gen[FooAction]
  given JsonEncoder[FooAction] = DeriveJsonEncoder.gen[FooAction]
end FooAction

object ZioJson102 extends App:
  import FooAction.given

  val json = FooAction("test").toJson
  println(json)

  val fooAction = json.fromJson
  println(fooAction)
  
end ZioJson102  
