import Common._

name := "event-sourced-dice-game"

version := "0.1"

// Using fork of reactive-rabbit until new version of original project is available.
// Uncomment original dependencies and remove this one once it's ready.
//lazy val reactiveRabbitFork = RootProject(uri("git://github.com/darrenfu/reactive-rabbit.git"))

lazy val game = project.in(file("game"))
  .settings(commonSettings: _*)
  .settings(
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-actor"       % akkaVersion,
      "com.typesafe.akka" %% "akka-slf4j"       % akkaVersion,
      "com.typesafe.akka" %% "akka-persistence" % akkaVersion,
      "com.typesafe.akka" %% "akka-stream"      % akkaVersion,
      "io.scalac"         %% "reactive-rabbit"  % reactiveRabbitVersion,
      "io.spray"          %% "spray-routing"    % sprayVersion,
      "io.spray"          %% "spray-can"        % sprayVersion,
      "org.json4s"        %% "json4s-native"    % json4sVersion,
      "org.scalatest"     %% "scalatest"        % "3.0.1" % "test",
      "ch.qos.logback"     % "logback-classic"  % logbackVersion
    ),
    fork := true
  )
//  .dependsOn(reactiveRabbitFork)

lazy val statistics = project.in(file("statistics"))
  .settings(commonSettings: _*)
  .settings(
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-actor"       % akkaVersion,
      "com.typesafe.akka" %% "akka-slf4j"       % akkaVersion,
      "com.typesafe.akka" %% "akka-persistence" % akkaVersion,
      "com.typesafe.akka" %% "akka-stream"      % akkaVersion,
      "io.spray"          %% "spray-routing"    % sprayVersion,
      "io.spray"          %% "spray-can"        % sprayVersion,
      "io.scalac"         %% "reactive-rabbit"  % reactiveRabbitVersion,
      "org.json4s"        %% "json4s-native"    % json4sVersion,
      "ch.qos.logback"     % "logback-classic"  % logbackVersion
    ),
    fork := true
  )
//  .dependsOn(reactiveRabbitFork)

lazy val webapp = project.in(file("webapp"))
  .settings(commonSettings: _*)
  .settings(
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-actor"       % akkaVersion,
      "com.typesafe.akka" %% "akka-stream"      % akkaVersion,
      "io.scalac"         %% "reactive-rabbit"  % reactiveRabbitVersion,
      "com.typesafe.play" %% "play-ws"          % playVersion,
      "org.webjars"       %% "webjars-play"     % playVersion,
      "org.webjars"        % "jquery"           % "2.1.3",
      "org.webjars"        % "angularjs"        % "1.3.11",
      "org.webjars"        % "bootstrap"        % "3.3.2"
    )
  )
  .enablePlugins(PlayScala)
//  .dependsOn(reactiveRabbitFork)
