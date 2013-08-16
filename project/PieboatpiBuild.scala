import sbt._
import sbt.Keys._

object PieboatpiBuild extends Build {

  val appDependencies = Seq(
    "pieboat.network" %% "pieboat-network-lib" % "0.1-SNAPSHOT"
  )

  lazy val pieboatpi = Project(
    id = "pieboat-pi",
    base = file("."),
    settings = Project.defaultSettings ++ Seq(
      name := "Pieboat-Pi",
      organization := "pieboat.pi",
      version := "0.1-SNAPSHOT",
      scalaVersion := "2.10.2",
      libraryDependencies ++= appDependencies
    )
  )
}
