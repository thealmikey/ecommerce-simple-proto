name := "scala2neo"

version := "0.1"

scalaVersion := "2.12.4"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http" % "10.1.0-RC1",
  "com.typesafe.akka" %% "akka-stream" % "2.5.8"
)

val circeVersion = "0.9.0"

libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser"
).map(_ % circeVersion)

libraryDependencies += "io.circe" %% "circe-optics" % circeVersion
// https://mvnrepository.com/artifact/de.heikoseeberger/akka-http-circe
libraryDependencies += "de.heikoseeberger" %% "akka-http-circe" % "1.19.0"

val monocleVersion = "1.5.0" // 1.5.0-cats based on cats 1.0.x

libraryDependencies ++= Seq(
  "com.github.julien-truffaut" %% "monocle-core" % monocleVersion,
  "com.github.julien-truffaut" %% "monocle-macro" % monocleVersion,
  "com.github.julien-truffaut" %% "monocle-law" % monocleVersion % "test"
)

addCompilerPlugin(
  "org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full
)

libraryDependencies += "org.typelevel" %% "cats-core" % "1.0.1"

// Regular (JVM) Scala projects:
libraryDependencies += "com.wix" %% "accord-core" % "0.7.2"
libraryDependencies += "com.wix" %% "accord-scalatest" % "0.7.2" % "test"

libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.4"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.4" % "test"
libraryDependencies += "org.scalacheck" %% "scalacheck" % "1.13.4" % "test"
libraryDependencies += "org.scalamock" %% "scalamock" % "4.1.0" % Test
// https://mvnrepository.com/artifact/org.scalamock/scalamock-scalatest-support
libraryDependencies += "org.scalamock" %% "scalamock-scalatest-support" % "3.6.0" % Test

libraryDependencies ++= Seq(
  "com.michaelpollmeier" %% "gremlin-scala" % "3.3.0.5"
)
// https://mvnrepository.com/artifact/com.orientechnologies/orientdb-gremlin
libraryDependencies += "com.orientechnologies" % "orientdb-gremlin" % "2.2.32"

//libraryDependencies ++= Seq(
//  "org.apache.tinkerpop" % "neo4j-gremlin" % "3.3.1",
//  "org.neo4j" % "neo4j-tinkerpop-api-impl" % "0.7-3.2.3"
//)
//// https://mvnrepository.com/artifact/org.neo4j/neo4j
//libraryDependencies += "org.neo4j" % "neo4j" % "3.3.3"
//// https://mvnrepository.com/artifact/com.steelbridgelabs.oss/neo4j-gremlin-bolt
//libraryDependencies += "com.steelbridgelabs.oss" % "neo4j-gremlin-bolt" % "0.2.27"
//
