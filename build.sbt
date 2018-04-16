
lazy val implementation = 'files_streaminga

if (implementation == 'files_streaming) {
  javaOptions in run += "-Dconfig.resource=application-files.conf"
} else {
  javaOptions in run += "-Dconfig.resource=application-s3.conf"
}

Compile / run / fork := true

/*
assemblyMergeStrategy in assembly := {
  case PathList("META-INF", "MANIFEST.MF") => MergeStrategy.discard
  case x => MergeStrategy.first
}
*/

lazy val root = (project in file("."))
  //  .aggregate(files_streaming)
  .settings(
  //  , aggregate in run := true
  //  , aggregate in compile := true
).enablePlugins(Common)
  .dependsOn(
    common_streaming
    , if (implementation == 'files_streaming) files_streaming else s3_streaming
  )

lazy val api_streaming = (project in file("streaming/api"))
  .enablePlugins(Common)
  .dependsOn(common_streaming)

lazy val common_streaming = (project in file("streaming/common"))
  .enablePlugins(Common)

lazy val core_streaming = (project in file("streaming/core"))
  .enablePlugins(Common)
  .dependsOn(api_streaming, common_streaming)

lazy val files_streaming = (project in file("streaming/impl/files"))
  .enablePlugins(Common)
  .dependsOn(core_streaming, common_streaming)

lazy val s3_streaming = (project in file("streaming/impl/s3"))
  .enablePlugins(Common)
  .dependsOn(core_streaming, common_streaming)

name := "files-ingestor"
organization := "com.bpstreaming"

scalaVersion := "2.12.5"

resolvers += Resolver.sonatypeRepo("releases")
resolvers += Resolver.sonatypeRepo("snapshots")


val akkaVersion = "2.5.11"
val akkaHttpVersion = "10.1.1"

libraryDependencies ++= Seq(

  "io.swagger" % "swagger-jaxrs" % "1.5.18",
  "com.github.swagger-akka-http" %% "swagger-akka-http" % "0.14.0",

  "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,

  "ch.megard" %% "akka-http-cors" % "0.3.0",

  "org.slf4j" % "slf4j-simple" % "1.7.25",

 // "com.chuusai" %% "shapeless" % "2.3.3",
)


addCommandAlias("uc", ";updateClassifiers")
addCommandAlias("ucr", ";updateClassifiers;run")
addCommandAlias("r", ";run")
addCommandAlias("c", ";compile")
addCommandAlias("rl", ";reload")
addCommandAlias("rlc", ";reload;clean")

resolvers ++= Seq(
  Resolver.sonatypeRepo("releases"),
  Resolver.sonatypeRepo("snapshots")
)
