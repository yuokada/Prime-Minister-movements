ThisBuild / scalaVersion     := "2.13.3"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.pmmovements"
ThisBuild / organizationName := "pmmovements"

lazy val root = (project in file("."))
  .settings(
    name := "Prime Ministers movement",
      libraryDependencies ++= Seq(
        "com.opencsv" % "opencsv" % "5.3",
        "org.scalatest" %% "scalatest" % "3.2.2"
      )
)
