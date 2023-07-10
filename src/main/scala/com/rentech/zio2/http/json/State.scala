package com.rentech.zio2.http.json

final case class State(
    icao24: String,
    callsign: Option[String],
    originCountry: String,
    timePosition: Option[Long],
    lastContact: Long,
    longitude: Option[Double],
    latitude: Option[Double],
    baroAltitude: Option[Double],
    onGround: Boolean,
    velocity: Option[Double],
    trueTrack: Option[Double],
    verticalRate: Option[Double],
    sensors: Option[List[Long]],
    geoAltitude: Option[Double],
    squawk: Option[String],
    spi: Boolean,
    positionSource: Int
)
