package pmmovements

import java.nio.file.{Files, Paths}
import java.time.ZonedDateTime

import scala.io.Source
import scala.util.Using

case class ProcessorConfig(
    srcDirectory: String,
    dstDirectory: String
)

case class PrimeMinisterAction(var action: String, actionDate: ZonedDateTime, actionDateString: Option[String] = None) {
//  def this(action: String, actionDate: ZonedDateTime) = {
//    this(action.replace("\t", " | "), actionDate, None)
//  }
  this.action = action.replace("\t", " | ")
}

object PrimeMinisterSplitter {

  // Utility functions
  def getYearFromFilename(filename: String): Int = {
    val basename = Paths.get(filename).getFileName()
    basename.toString.slice(0, 4).toInt
  }

  def main(args: Array[String]): Unit = {
    val config = ProcessorConfig("./1stprocesseed", dstDirectory = "./finalProcessed")

    val srcDirectory = Paths.get(config.srcDirectory)
    if (!Files.isDirectory(srcDirectory)) {
      println(s"${srcDirectory} is not a directory! Please set a directory")
      return
    }

    Files
      .list(srcDirectory).sorted
//      .filter(filename => !filename.getFileName.toString.startsWith("2020"))
      .forEach(f => {
        println("DEBUG: " + f)
        val year      = getYearFromFilename(f.getFileName.toString)
        val actParser = new ActionLineParser(year)
        Using.Manager { use =>
          val sourceFile = use(Source.fromFile(name = f.toString))
          val dstTsvFile = config.dstDirectory + "/" + f.getFileName.toString.replace("csv", "tsv")
          val printer    = use(new ActionsPrinter(dstTsvFile))

          sourceFile.getLines().zipWithIndex.foreach { case (l, i) =>
            l.length match {
              case 0 => // Skip empty lines
              case _ => {
                val action = actParser.parser(l)
                action match {
                  case Some(row) => printer.writeNext(row)
                  case None      => println(s"DEBUG: ${i} ${l}")
                }
              }
            }
          }
        }
      })
  }
}
