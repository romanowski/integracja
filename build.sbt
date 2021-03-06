name := """integracja"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  ws
)

libraryDependencies ++= {
  Seq("drools-compiler", "drools-core")
    .map("org.drools" % _ % "5.4.0.Final") ++
    Seq("nz.ac.waikato.cms.weka" % "weka-stable" % "3.6.11")
}
