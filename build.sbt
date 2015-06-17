name := "spray demo"

version := "1.0"

scalaVersion := "2.11.4"


libraryDependencies ++= Seq(
				"io.spray"            %% "spray-can"        %  "1.3.1",
				"io.spray"            %% "spray-routing"    % "1.3.1",
				"io.spray"           %% "spray-json"       % "1.3.0",
				"com.typesafe.akka"  %% "akka-actor"       % "2.3.6",
				"io.spray" %% "spray-testkit" % "1.3.1" % "test",
				"org.specs2" % "specs2_2.11" % "2.4.7",
				"org.scalatest" % "scalatest_2.11" % "2.2.4" % "test"
				       )

resolvers += "spray repo" at "http://repo.spray.io"

resolvers += "Scalaz Bintray Repo" at "https://dl.bintray.com/scalaz/releases"