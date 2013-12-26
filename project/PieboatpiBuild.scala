import sbt._
import sbt.Keys._
import com.github.retronym.SbtOneJar.oneJarSettings

object PieboatpiBuild extends Build {

  val appDependencies = Seq(
    "pieboat.network" %% "pieboat-network-lib" % "0.1-SNAPSHOT",
    "com.pi4j" % "pi4j-core" % "0.0.5"
  )

  lazy val pieboatpi = Project(
    id = "pieboat-pi",
    base = file("."),
    settings = Project.defaultSettings ++ oneJarSettings ++ Seq(
      name := "Pieboat-Pi",
      organization := "pieboat.pi",
      version := "0.1-SNAPSHOT",
      scalaVersion := "2.10.2",
      libraryDependencies ++= appDependencies,
      scalacOptions ++= Seq(
        "-language:postfixOps", "-unchecked", "-deprecation", "-feature"
      )
    )
  )
}
