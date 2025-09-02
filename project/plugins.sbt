addSbtPlugin("com.eed3si9n" % "sbt-buildinfo" % "0.12.0")
addSbtPlugin("com.eed3si9n" % "sbt-assembly"  % "0.15.0")
// For setting explicit versions for each commit
addSbtPlugin("com.dwijnand" % "sbt-dynver" % "4.1.1")

addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.5.5")
//addSbtPlugin( "org.scalatest" %% "scalatest" % "3.0.5")
//addSbtPlugin("com.artima.supersafe" % "sbtplugin" % "1.1.10")
addSbtPlugin("org.xerial.sbt" % "sbt-pack"     % "0.19")
scalacOptions ++= Seq("-deprecation", "-feature")


ThisBuild / libraryDependencySchemes += "org.scala-lang.modules" %% "scala-xml" % VersionScheme.Always
// Available since sbt-1.4.0
addDependencyTreePlugin
