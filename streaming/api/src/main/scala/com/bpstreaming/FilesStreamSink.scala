package com.bpstreaming

import akka.stream.scaladsl.Sink
import akka.util.ByteString

import scala.concurrent.Future

trait FilesStreamSink[T] {
  def outputStreamSink(fileName: String): Sink[ByteString, Future[T]]
}
