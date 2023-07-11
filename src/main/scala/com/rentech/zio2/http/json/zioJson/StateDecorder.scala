package com.rentech.zio2.http.json.zioJson

import com.rentech.zio2.http.json.State
import zio.json.JsonDecoder

object States {
  given decoder: JsonDecoder[State] =
    JsonDecoder[
      (String,
          Option[String],
          String,
          Option[Long],
          Long,
          Option[Double],
          Option[Double],
          Option[Double],
          Boolean,
          Option[Double],
          Option[Double],
          Option[Double],
          Option[List[Long]],
          Option[Double],
          Option[String],
          Boolean,
          Int,
        ),
    ].map {
      case (
             icao24,
             callsign,
             originCountry,
             timePosition,
             lastContact,
             longitude,
             latitude,
             baroAltitude,
             onGround,
             velocity,
             trueTrack,
             verticalRate,
             sensors,
             geoAltitude,
             squawk,
             spi,
             positionSource,
           ) =>
        State(
          icao24,
          callsign,
          originCountry,
          timePosition,
          lastContact,
          longitude,
          latitude,
          baroAltitude,
          onGround,
          velocity,
          trueTrack,
          verticalRate,
          sensors,
          geoAltitude,
          squawk,
          spi,
          positionSource,
        )
    }
}
