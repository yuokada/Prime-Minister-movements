import sbt.Def.spaceDelimited

import java.nio.charset.StandardCharsets
import java.nio.file.Files.newBufferedWriter
import java.nio.file.Paths
import java.time.ZoneId
import java.time.format.DateTimeFormatter

ThisBuild / scalaVersion := "2.13.15"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "com.pmmovements"
ThisBuild / organizationName := "pmmovements"
lazy val AIRFRAME_VERSION = "24.12.2"

lazy val root = (project in file("."))
  .settings(
    name := "Prime Ministers movement",
    libraryDependencies ++= Seq(
      "com.opencsv"         % "opencsv"        % "5.9",
      "org.wvlet.airframe" %% "airframe-codec" % AIRFRAME_VERSION,
      "org.wvlet.airframe" %% "airframe-json"  % AIRFRAME_VERSION,
      "org.wvlet.airframe" %% "airframe-log"   % AIRFRAME_VERSION,
      "org.scalatest"      %% "scalatest"      % "3.2.19"
    )
  )
  .settings {
    crawl := {
      val yearAndMonth: String = spaceDelimited("<args>").parsed match {
        case args if args.mkString(" ") == "last month" => {
          val lastMonth = java.time.LocalDate.now(ZoneId.of("Asia/Tokyo"))
            .minusMonths(1)
          lastMonth.format(DateTimeFormatter.ofPattern("YYYYMM"))
        }
        case args if args.nonEmpty => args.head
        case _ =>
          val today = java.time.LocalDate.now(ZoneId.of("Asia/Tokyo"))
          today.format(DateTimeFormatter.ofPattern("YYYYMM"))
      }
      val url = s"https://www.nhk.or.jp/politics/souri/csv/${yearAndMonth}.csv"
      println(s"Fetch from ${url} ...")
      val response = scala.io.Source.fromURL(url)(StandardCharsets.UTF_16)

      val writer = newBufferedWriter(Paths.get(s"original/${yearAndMonth}.csv"), StandardCharsets.UTF_8)
      response.mkString.split("\n").foreach(line => writer.write(line.stripTrailing() + "\n"))
      writer.close()
    }
  }

lazy val crawl = inputKey[Unit]("Fetch a CSV file from web")
