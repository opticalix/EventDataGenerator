name := "Data-Generator"

version := "0.1"

scalaVersion := "2.12.8"

resolvers += Resolver.mavenLocal

libraryDependencies += "joda-time" % "joda-time" % "2.10.1"
libraryDependencies += "com.github.nscala-time" %% "nscala-time" % "2.20.0"
libraryDependencies += "net.sourceforge.argparse4j" % "argparse4j" % "0.8.1"
libraryDependencies += "org.apache.kafka" % "kafka-clients" % "2.0.0"
libraryDependencies += "com.fasterxml.jackson.core" % "jackson-databind" % "2.9.8"
libraryDependencies += "org.slf4j" % "slf4j-simple" % "1.7.25"
libraryDependencies += "com.opticalix" % "common" % "1.0-SNAPSHOT"
