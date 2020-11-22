package example

import java.io.FileWriter
import java.time.format.DateTimeFormatter

import com.opencsv.CSVWriterBuilder

class ActionsPrinter(dstFilename: String) {

  private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

  private val writer    = new FileWriter(dstFilename)
  private val csvWriter = new CSVWriterBuilder(writer).withSeparator('\t').build()

  //  val beanToCsv: Nothing = new Nothing(writer).build
  //  beanToCsv.write(beans)
  def writeNext(row: PrimeMinisterAction): Unit = {
    val rowArray: Array[String] = Seq(row.actionDate.format(formatter), row.action).toArray
    csvWriter.writeNext(rowArray)
  }
}
