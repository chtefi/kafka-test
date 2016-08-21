name := "kafka-test"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies += "org.apache.kafka" %% "kafka" % "0.9.0.0"

javaOptions in run += "-verbose:gc -XX:+PrintFlagsFinal"


resolvers += Resolver.mavenLocal
