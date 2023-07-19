package com.rentech.zio2.http.json.json4s

import org.json4s.JsonDSL.*
import org.json4s.*
import org.json4s.native.*

import com.rentech.zio2.http.json.State

given formats: Formats = DefaultFormats

class StateSerializer
    extends CustomSerializer[State](format =>
      (
        {
          case JArray(jsList) =>
            val icao24         = jsList(0).extract[String]
            val callsign       = jsList(1).extractOpt[String]
            val originCountry  = jsList(2).extract[String]
            val timePosition   = jsList(3).extractOpt[Long]
            val lastContact    = jsList(4).extract[Long]
            val longitude      = jsList(5).extractOpt[Double]
            val latitude       = jsList(6).extractOpt[Double]
            val baroAltitude   = jsList(7).extractOpt[Double]
            val onGround       = jsList(8).extract[Boolean]
            val velocity       = jsList(9).extractOpt[Double]
            val trueTrack      = jsList(10).extractOpt[Double]
            val verticalRate   = jsList(11).extractOpt[Double]
            val sensors        = jsList(12).extractOpt[List[Long]]
            val geoAltitude    = jsList(13).extractOpt[Double]
            val squawk         = jsList(14).extractOpt[String]
            val spi            = jsList(15).extract[Boolean]
            val positionSource = jsList(16).extract[Int]

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
              sensors.flatMap(lst => if (lst.isEmpty) None else Some(lst)),
              geoAltitude,
              squawk,
              spi,
              positionSource,
            )
        },
        {
          case state: State =>
            val callsign       = state.callsign.fold(JNull)(JString.apply)
            val icao24         = JString(state.icao24)
            val originCountry  = JString(state.originCountry)
            val timePosition   = state.timePosition.fold(JNull)(JLong.apply)
            val lastContact    = JLong(state.lastContact)
            val longitude      = state.longitude.fold(JNull)(JDouble.apply)
            val latitude       = state.latitude.fold(JNull)(JDouble.apply)
            val baroAltitude   = state.baroAltitude.fold(JNull)(JDouble.apply)
            val onGround       = JBool(state.onGround)
            val velocity       = state.velocity.fold(JNull)(JDouble.apply)
            val trueTrack      = state.trueTrack.fold(JNull)(JDouble.apply)
            val verticalRate   = state.verticalRate.fold(JNull)(JDouble.apply)
            val geoAltitude    = state.geoAltitude.fold(JNull)(JDouble.apply)
            val sensors        =
              state.sensors.fold(JNull)(lst => JArray.apply(lst.map(JLong.apply)))
            val squawk         = state.squawk.fold(JNull)(JString.apply)
            val spi            = JBool(state.spi)
            val positionSource = JInt(state.positionSource)

            val lst = List(
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
            JArray(lst)
        },
      ),
    )
