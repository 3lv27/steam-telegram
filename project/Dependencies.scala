import sbt._

object Dependencies {

  object Versions {
    val play = "2.9.0"

  }
  val production = Seq(
    "com.github.nscala-time"      %% "nscala-time"         % "2.22.0",
    "com.github.blemale"          %% "scaffeine" % "3.1.0" % "compile",
    "ch.qos.logback"              % "logback-classic"      % "1.2.3", // Logging backend implementation
    "com.typesafe.scala-logging"  %% "scala-logging"       % "3.7.2", // SLF4J Scala wrapper
    "net.logstash.logback"        % "logstash-logback-encoder" % "4.11", // Log JSON encoder

    "com.typesafe"           % "config"          % "1.4.0",
    "com.typesafe.play"      %% "play-cache"     % "2.8.2",
    "com.typesafe.play"      %% "play-json"      % Versions.play,
  )

  val test = Seq(
    "org.scalatest"     %% "scalatest"    % "3.1.1" % Test,
    "org.scalamock"     %% "scalamock"    % "4.4.0" % Test,

  )

}
