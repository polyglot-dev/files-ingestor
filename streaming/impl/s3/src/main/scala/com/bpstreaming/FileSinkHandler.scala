package com.bpstreaming

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.alpakka.s3.scaladsl.{MultipartUploadResult, S3Client}
import akka.stream.alpakka.s3.{MemoryBufferType, S3Settings}
import akka.stream.scaladsl.Sink
import akka.util.ByteString
import com.amazonaws.auth.{AWSStaticCredentialsProvider, BasicAWSCredentials}

import scala.concurrent.{ExecutionContext, Future}

class FileSinkHandler(implicit conf: Config, executionContext: ExecutionContext,
                      materializer: ActorMaterializer, system: ActorSystem) extends FilesHandler[MultipartUploadResult] {

  val awsCredentials = new AWSStaticCredentialsProvider(
    new BasicAWSCredentials(conf.getString("accessKey"),conf.getString("secretKey"))
  )

  val settings = new S3Settings(MemoryBufferType, None, awsCredentials, conf.getString("s3Region"), false)
  val s3Client = new S3Client(settings)(system, materializer)

  def outputStreamSink(fileName: String): Sink[ByteString, Future[MultipartUploadResult]] = {
    s3Client.multipartUpload(
      //bucket
      conf.getString("bucketName")
      //bucketKey
      ,fileName
    )
  }

}