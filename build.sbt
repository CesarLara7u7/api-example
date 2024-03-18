
ThisBuild / scalaVersion := "2.13.12"
ThisBuild / organization := "com.cesar"


ThisBuild / scalafixScalaBinaryVersion := "2.13"

val AkkaVersion = "2.9.0"
val AkkaHttpVersion = "10.6.0"
val swaggerVersion = "2.2.18"
val jacksonVersion = "2.17.0"

Compile/mainClass := Some("com.cesar.MainApp")
lazy val commonSettings = Seq(
  resolvers += "Akka library repository".at("https://repo.akka.io/maven")
)

lazy val commonDependencies = Seq(
  "com.fasterxml.uuid" % "java-uuid-generator" % "4.3.0",
)


lazy val business = (project in file("business")).settings(
  name := "example-business",
  libraryDependencies ++= Seq(
    "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion,
    "com.typesafe.akka" %% "akka-stream" % AkkaVersion,
    "com.typesafe.akka" %% "akka-http" % AkkaHttpVersion,
    "com.typesafe.akka" %% "akka-http-spray-json" % AkkaHttpVersion,
  ) ++ swaggerDependencies,
  commonSettings
)

lazy val data = (project in file("data"))
  .dependsOn(business)
  .settings(
    name := "example-data",
    resolvers += "typesafe".at("https://repo1.maven.org/maven2/"),
    libraryDependencies ++= Seq(
      "com.typesafe.slick" %% "slick" % "3.4.1",
      "org.slf4j" % "slf4j-nop" % "2.0.9",
      "org.mongodb.scala" %% "mongo-scala-driver" % "4.11.1"
    ),
    commonSettings
  )

val swaggerDependencies = Seq(
  "jakarta.ws.rs" % "jakarta.ws.rs-api" % "3.1.0",
  "com.github.swagger-akka-http" %% "swagger-akka-http" % "2.11.0",
  "com.github.swagger-akka-http" %% "swagger-scala-module" % "2.12.0",
  "com.github.swagger-akka-http" %% "swagger-enumeratum-module" % "2.9.0",
  "com.fasterxml.jackson.module" %% "jackson-module-scala" % jacksonVersion,
  "io.swagger.core.v3" % "swagger-jaxrs2-jakarta" % swaggerVersion
)

lazy val api = (project in file("api"))
  .settings(
    name := "example-api",
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion,
      "com.typesafe.akka" %% "akka-stream" % AkkaVersion,
      "com.typesafe.akka" %% "akka-http" % AkkaHttpVersion,
      "net.codingwell" %% "scala-guice" % "7.0.0",
    ) ++ swaggerDependencies,
    commonSettings
  ).dependsOn(business, data)


lazy val root = (project in file(".")).
  settings(
    name := "ejemplo_api",
    commonSettings,
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion,
      "com.typesafe.akka" %% "akka-stream" % AkkaVersion,
      "com.typesafe.akka" %% "akka-http" % AkkaHttpVersion,
      "ch.qos.logback" % "logback-classic" % "1.4.14"
    )
  ).dependsOn(business, api, data)
  .aggregate(business, api, data)
