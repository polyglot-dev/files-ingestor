package com.bpstreaming.services

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives
import akka.stream.ActorMaterializer
import com.bpstreaming.{Config, Consul, DefaultJsonFormats, TCheck}

import scala.concurrent.ExecutionContext


class HealthCheck(
    implicit executionContext: ExecutionContext,
    materializer: ActorMaterializer,
    system: ActorSystem
) extends Directives
    with DefaultJsonFormats {

  val route = check

  def check =
    path("check") {
      get {
        complete("ok")
      }
    }

  def register(conf: Config, localhost: String): Unit = {
    val params = Consul(
      conf.getString("consulServiceName"),
      List("primary", "v1"),
      conf.getString("consulRegister"),
      9000,
      false,
      TCheck("90m", "3s", s"http://$localhost:${conf.getInt("servicePort")}/api/v1/files/check")
    )

    Marshal(params).to[RequestEntity] flatMap { entity =>
      val request = HttpRequest(
        method = HttpMethods.PUT,
        uri = s"${conf.getString("consulRegisterPath")}:8500/v1/agent/service/register",
        entity = entity
      )

      Http().singleRequest(request)

    }

  }

}
