package com.rentech.zio2.http.tapir.tapir102

import sttp.tapir.{emptyOutputAs, endpoint, oneOf, oneOfVariant, query, statusCode, stringBody}
import sttp.tapir.PublicEndpoint

import sttp.tapir.given
import sttp.tapir.json.zio.*
import sttp.tapir.generic.auto.*

import sttp.tapir.server.metrics.prometheus.PrometheusMetrics
import sttp.tapir.swagger.bundle.SwaggerInterpreter
import sttp.tapir.ztapir.{RichZEndpoint, ZServerEndpoint}

import zio.{Task, ZIO}
import zio.json.*

import sttp.model.StatusCode

enum AgeError:
  case WrongValue(invalid: String)
end AgeError

object AgeError:
  given wrongValueEncoder: zio.json.JsonEncoder[WrongValue] = DeriveJsonEncoder.gen[WrongValue]
  given wrongValueDecoder: zio.json.JsonDecoder[WrongValue] = DeriveJsonDecoder.gen[WrongValue]
end AgeError

object Routes:
  val prometheusMetrics: PrometheusMetrics[Task] = PrometheusMetrics.default[Task]()

  val helloEndpoint: PublicEndpoint[String, Unit, String, Any] = endpoint
    .get
    .in("hello")
    .in(query[String]("name"))
    .out(stringBody)

  val allEndpoint: PublicEndpoint[Unit, Unit, String, Any] = endpoint
    .get
    .in("all")
    .out(stringBody)

  import AgeError.*
  val ageEndPoint: PublicEndpoint[Int, AgeError, String, Any] = endpoint
    .get
    .in("age")
    .in(query[Int]("value"))
    .errorOut(
      oneOf[AgeError](
        oneOfVariant(
          statusCode(StatusCode.BadRequest)
            .and(jsonBody[WrongValue].description("Age must be an integer between 18 and 140")),
        ),
      ),
    )
    .out(stringBody)

  val helloServerEndpoint: ZServerEndpoint[Any, Any] =
    helloEndpoint.serverLogicSuccess(user => ZIO.succeed(s"Hello ${user}"))

  val allServerEndpoint: ZServerEndpoint[Any, Any] =
    allEndpoint.serverLogicSuccess(_ => Models.getAll())

  val ageServerEndPoint: ZServerEndpoint[Any, Any] =
    ageEndPoint.zServerLogic: age =>
      if age < 18 then ZIO.fail(AgeError.WrongValue("age cannot be less than 18"))
      else if age > 140 then ZIO.fail(AgeError.WrongValue("age cannot be greater than 140"))
      else ZIO.succeed(s"age is $age")
    

  val docEndpoints: List[ZServerEndpoint[Any, Any]] = SwaggerInterpreter()
    .fromServerEndpoints[Task](
      List(helloServerEndpoint, allServerEndpoint, ageServerEndPoint),
      "tapir101",
      "1.0.0",
    )

  val all = List(helloServerEndpoint, allServerEndpoint, ageServerEndPoint) ++ docEndpoints
