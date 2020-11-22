package example

import java.io.FileWriter
import java.time.format.DateTimeFormatter

import com.opencsv.CSVWriterBuilder

class ActionsPrinter(dstFilename: String) extends AutoCloseable {

  private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
  private val csvWriter = new CSVWriterBuilder(new FileWriter(dstFilename)).withSeparator('\t').build()

  def writeNext(row: PrimeMinisterAction): Unit = {
    val rowArray = Seq(row.actionDate.format(formatter), row.action).toArray
    csvWriter.writeNext(rowArray)
  }


  def close(): Unit ={
    csvWriter.flush()
    csvWriter.close()
    println(s"DEBUG: $dstFilename is close!")
  }
}
