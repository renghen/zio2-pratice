package com.rentech.zio2.iron

import io.github.iltotore.iron.*
import io.github.iltotore.iron.constraint.any.StrictEqual
import io.github.iltotore.iron.constraint.numeric.Greater

object PositiveNumber:
  type GreaterEqual[V] = Greater[V] | StrictEqual[V]

  val x: Int :| GreaterEqual[0] = 1 // OK
  val y: Int :| GreaterEqual[0] = 1 // OK

end PositiveNumber
//   val z: Int :| GreaterEqual[0] = -1 // Compile-time error: (Should be greater than 0 | Should strictly equal to 0)
