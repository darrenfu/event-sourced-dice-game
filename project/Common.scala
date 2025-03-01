import sbt.Keys._

object Common {
  val commonSettings = Seq(
    scalaVersion := "2.11.11",
    crossScalaVersions := Seq("2.11.11", "2.12.0"),
    scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature", "-Xfatal-warnings")
  )

  val akkaVersion = "2.4.20"
//  val playVersion = "2.7.3"
  val playVersion = "2.5.18"
  val reactiveRabbitVersion = "1.1.4"
  val sprayVersion = "1.3.1"
  val json4sVersion = "3.2.11"
  val logbackVersion = "1.1.2"
}
