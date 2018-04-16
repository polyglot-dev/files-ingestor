package com.bpstreaming

import java.util.UUID

import akka.http.scaladsl.server.{Directives, Route}
import akka.stream.scaladsl.{Keep, Source}
import akka.stream.{ActorMaterializer, OverflowStrategy}
import akka.util.ByteString

import scala.concurrent.ExecutionContext
import scala.util.Try

abstract class FilesHandler[T](
                         implicit conf: Config, executionContext: ExecutionContext,
                         materializer: ActorMaterializer
                       ) extends Directives
  with DefaultJsonFormats
  with FilesStreamSink[T] {

  val route = upload

  def upload: Route =
    path("upload") {

      post {

        withoutSizeLimit {

          extractDataBytes { bytes: Source[ByteString, Any] =>
            extractRequestContext { req =>
              val fileName = req.request.headers
                .map(a => (a.name(), a.value()))
                .toMap
                .getOrElse("filename", UUID.randomUUID.toString)

              val bufferSizeBackpressure = Try {
                conf.getInt("bufferSize")
              }

              val inputStream: Source[ByteString, Any] = bufferSizeBackpressure
                .map(buffer => {
                  bytes.buffer(buffer, OverflowStrategy.backpressure)
                })
                .recover {
                  case _ =>
                    bytes
                }
                .get

              onComplete(

                inputStream
                  .toMat(outputStreamSink(fileName))(Keep.right)
                  .run()

              ) { ioResult: Try[T] =>
                if (ioResult.isSuccess) {
                  complete(s"File '$fileName' uploaded")
                } else {
                  failWith(ioResult.failed.get)
                }
              }

            }
          }
        }
      }

    }

}
