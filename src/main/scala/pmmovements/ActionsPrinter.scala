package pmmovements

import java.io.FileWriter
import java.nio.file.Files.newBufferedWriter
import java.nio.file.Paths
import java.time.format.DateTimeFormatter

import com.opencsv.CSVWriterBuilder
import wvlet.airframe.codec.MessageCodec
import wvlet.log.LogSupport

trait IFPrinter extends AutoCloseable with LogSupport {
  val dstFilename: String
  protected val lineSeparator = System.lineSeparator()

  protected val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
  def writeNext(row: PrimeMinisterAction): Unit
  def close(): Unit
}

class ActionsPrinter(val dstFilename: String) extends IFPrinter {

//  private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
  private val csvWriter = new CSVWriterBuilder(new FileWriter(dstFilename)).withSeparator('\t').build()

  override def writeNext(row: PrimeMinisterAction): Unit = {
    val rowArray = Seq(row.actionDate.format(formatter), row.action).toArray
    csvWriter.writeNext(rowArray)
  }

  override def close(): Unit = {
    csvWriter.flush()
    csvWriter.close()
    info(s"${dstFilename} was closed")
  }
}

class ActionJSONLPrinter(val dstFilename: String) extends IFPrinter {
  private val writer = newBufferedWriter(Paths.get(dstFilename))

  private val codec = MessageCodec.of[PrimeMinisterAction]

  override def writeNext(row: PrimeMinisterAction): Unit = {
    val json = codec.toJson(row.copy(actionDateString = Some(row.actionDate.format(DateTimeFormatter.ISO_INSTANT))))
    writer.write(json + lineSeparator)
  }

  override def close(): Unit = {
    writer.close()
    debug(s"${dstFilename} was closed")
  }
}
