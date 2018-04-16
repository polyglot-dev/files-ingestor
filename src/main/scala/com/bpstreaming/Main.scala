package com.bpstreaming

import java.net.InetAddress
import java.util
import java.util.Properties

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.RouteConcatenation
import akka.stream.ActorMaterializer
import ch.megard.akka.http.cors.scaladsl.CorsDirectives.cors
import com.bpstreaming.services.HealthCheck

import scala.concurrent.ExecutionContextExecutor
import scala.io.StdIn


object Main extends App with RouteConcatenation with DefaultJsonFormats {
  implicit val system: ActorSystem = ActorSystem("conectors")
  //sys.addShutdownHook(system.terminate())

  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher
  implicit val conf: Config = new Config(this.getClass.getClassLoader)

  val consulHealtCheck = new HealthCheck()
  val filesStreaming = new FileSinkHandler

  val routes =
    cors()(
      pathPrefix("api") {
        pathPrefix("v1") {
          pathPrefix("files") {
            filesStreaming.route ~
            consulHealtCheck.route
          }
        }
      }
    )

  val localhost: String = InetAddress.getLocalHost.getHostAddress

  consulHealtCheck.register(conf, localhost)

  val bindingFuture = Http().bindAndHandle(routes, localhost, conf.getInt("servicePort"))

  println(s"Server online at http://$localhost:${conf.getInt("servicePort")}/\nPress RETURN to stop...")

  StdIn.readLine() // let it run until user presses return
  bindingFuture
    .flatMap(_.unbind()) // trigger unbinding from the port
    .onComplete(_ => system.terminate()) // and shutdown when done

}
