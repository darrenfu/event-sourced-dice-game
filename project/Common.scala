import sbt.Keys._

object Common {
  val commonSettings = Seq(
    scalaVersion := "2.12.3",
    scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature", "-Xfatal-warnings")
  )

  val akkaVersion = "2.4.17"
  val playVersion = "2.6.6"
//  val akkaStreamVersion = "1.0-M3"
//  val reactiveRabbitVersion = "0.2.2"
  val sprayVersion = "1.3.1"
  val json4sVersion = "3.2.11"
  val logbackVersion = "1.1.2"
}
