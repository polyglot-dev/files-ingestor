
name := "S3"
organization := "com.bpstreaming"

scalaVersion := "2.12.5"

resolvers += Resolver.sonatypeRepo("releases")
resolvers += Resolver.sonatypeRepo("snapshots")

val akkaVersion = "2.5.11"
val akkaHttpVersion = "10.1.1"

libraryDependencies ++= Seq(
  "org.slf4j" % "slf4j-simple" % "1.7.25",
  "com.lightbend.akka" %% "akka-stream-alpakka-s3" % "0.15",

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

libraryDependencies += "com.chuusai" %% "shapeless" % "2.3.3"