import sbt.Keys.{scalaVersion, _}
import sbt._
import sbt.plugins.JvmPlugin

/**
  * Settings that are common to all the SBT projects
  */
object Common extends AutoPlugin {
  override def trigger = allRequirements

  override def requires: sbt.Plugins = JvmPlugin

  override def projectSettings = Seq(
    organization := "com.bpstreaming"
    , version := "2.0"
    , javacOptions ++= Seq("-source", "1.8", "-target", "1.8")
    , scalacOptions ++= Seq(
      "-encoding"
      , "UTF-8" // yes, this is 2 args
      , "-target:jvm-1.8"
      , "-deprecation"
      , "-feature"
      , "-unchecked"
      //      , "-Xlint"
      //      , "-Xfatal-warnings"
      , "-Yno-adapted-args"
      , "-Ywarn-numeric-widen"
      , "-Ypartial-unification"
    )
    , scalacOptions in Test ++= Seq("-Yrangepos")
    , autoAPIMappings := true
    , scalaVersion := "2.12.5"
  )
}
