package example

import java.nio.file.{Files, Paths}
import java.time.ZonedDateTime

import scala.io.Source

case class ProcessorConfig(
    OriginalDirectory: String,
    ProcessedDirectory: String
)

object PrimeMinisterSplitter {

  case class PrimeMinisterAction(action: String, actionDate: ZonedDateTime, actionDateString: Option[String] = None)

  // Utility functions
  def getYearFromFilename(filename: String): Int = {
    val basename = Paths.get(filename).getFileName()
    basename.toString.slice(0, 4).toInt
  }

  def main(args: Array[String]): Unit = {
    val config = ProcessorConfig("./1stprocesseed", ProcessedDirectory = "./finalProcessed")

    val srcDirectory = java.nio.file.Paths.get(config.OriginalDirectory)
    if (!Files.isDirectory(srcDirectory)) {
      println(s"${srcDirectory} is not a directory! Please set a directory")
      return
    }

    Files
      .list(srcDirectory).sorted
//      .filter(filename => !filename.getFileName.toString.startsWith("2020"))
      .forEach(f => {
        println("DEBUG: " + f)
        val year = getYearFromFilename(f.getFileName.toString)
        // https://qiita.com/ka2kama/items/cd846b15fbb56cdbc9ea
        // https://medium.com/@dkomanov/scala-try-with-resources-735baad0fd7d
        val actParser = new ActionLineParser(year)
        val source    = Source.fromFile(name = f.toString)
        try {
          source.getLines().zipWithIndex.foreach { case (l, i) =>
            l.length match {
              case 0 => // Nothing
              case _ => {
                val action = actParser.parser(l)
                action match {
                  case Some(_) =>
                  case None    => println(s"DEBUG: ${i} ${l}")
                }
              }
            }
          }
        } finally {
          source.close()
        }
      })
  }
}
