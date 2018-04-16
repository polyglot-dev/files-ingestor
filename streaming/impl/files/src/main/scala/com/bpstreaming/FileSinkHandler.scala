package com.bpstreaming

import java.io.{BufferedOutputStream, FileOutputStream}

import akka.stream.scaladsl.{Sink, StreamConverters}
import akka.stream.{ActorMaterializer, IOResult}
import akka.util.ByteString

import scala.concurrent.{ExecutionContext, Future}

class FileSinkHandler(implicit conf: Config, executionContext: ExecutionContext,
                      materializer: ActorMaterializer) extends FilesHandler[IOResult] {


  def outputStreamSink(fileName: String): Sink[ByteString, Future[IOResult]] = {
    StreamConverters.fromOutputStream(
         () => new BufferedOutputStream(new FileOutputStream(s"${conf.getString("fileOutputStreamPrefix")}$fileName"))
       )
  }

}
