name := "gibwsl"

version := "0.1"

scalaVersion := "2.13.2"

mainClass := Some("Main")

libraryDependencies ++= Seq("com.typesafe.akka" %% "akka-actor-typed" % "2.6.5",
  "com.typesafe.akka" %% "akka-stream" % "2.6.5",
  "com.typesafe.akka" %% "akka-http" % "10.1.12",
  "ch.qos.logback" % "logback-classic" % "1.1.3" % Runtime,
  "com.github.pureconfig" %% "pureconfig" % "0.12.3",
  "io.circe" %% "circe-core" % "0.13.0",
  "io.circe" %% "circe-generic" % "0.13.0",
  "io.circe" %% "circe-parser" % "0.13.0",
  "org.tpolecat" %% "doobie-core" % "0.8.8")