scalaVersion := "2.12.1"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http" % "10.0.4",
  "net.sourceforge.htmlunit" % "htmlunit" % "2.25"
)

enablePlugins(JavaAppPackaging)
