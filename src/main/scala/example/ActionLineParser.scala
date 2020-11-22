package example

import java.time.format.DateTimeFormatter
import java.time.{ZoneId, ZonedDateTime}

import example.PrimeMinisterSplitter.{getYearFromFilename, PrimeMinisterAction}

import scala.util.matching.Regex

class ActionFormatter() {
  private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

  def formatWithTab(row: PrimeMinisterAction): String = {
    format(row, "\t")
  }

  def format(row: PrimeMinisterAction, separator: String = "\t"): String = {
    // e.g: 4/30	1:35	(日本時間午前）政府専用機でＵＡＥ＝アラブ首長国連邦のアブダビ国際空港着
    s"${row.actionDate.format(DateTimeFormatter.ISO_INSTANT)}${separator}${row.action}"
  }

}

class ActionLineParser(year: Int) {
  private val baseFormat: Regex     = "\\d{1,2}/\\d{1,2}\\s+\\d{1,2}:\\d{1,2}\\s+.*$".r
  private val onlyDateFormat: Regex = "\\d{1,2}/\\d{1,2}\t+.*$".r // "\\d{1,2}/\\d{1,2}\t+.*$".r.findFirstIn(line)
  private val onlyTimeFormat: Regex = "\\d{1,2}:\\d{1,2}\t+.*$".r

  private val jst = ZoneId.of("Asia/Tokyo")

  // Current state
  var previousDate: ZonedDateTime = null

  def generalParser(line: String): PrimeMinisterAction = {
    // e.g: 4/30	1:35	(日本時間午前）政府専用機でＵＡＥ＝アラブ首長国連邦のアブダビ国際空港着
    val result = line.strip().split("\\s+", 3).toSeq

    val monthDate    = result(0).split("/").map(n => n.toInt).toSeq
    val (month, day) = (monthDate(0), monthDate(1))

    val hourDate = result(1).split(":").map(n => n.toInt).toSeq
    val (hh, dd) = (hourDate(0), hourDate(1))

    val zdt = ZonedDateTime.of(this.year, month, day, hh, dd, 0, 0, jst)
    val row = PrimeMinisterAction(action = result(2), actionDate = zdt)
    row
  }

  def onlyDateParser(line: String): PrimeMinisterAction = {
    // e.g: 4/30	(日本時間午前）政府専用機でＵＡＥ＝アラブ首長国連邦のアブダビ国際空港着
    val result = line.strip().split("\\s+", 2).toSeq

    val monthDate    = result(0).split("/").map(n => n.toInt).toSeq
    val (month, day) = (monthDate(0), monthDate(1))

    val zdt = ZonedDateTime.of(this.year, month, day, 9, 0, 0, 0, jst)
    val row = PrimeMinisterAction(action = result(1), actionDate = zdt)
    row
  }

  def onlyTimeParser(line: String): PrimeMinisterAction = {
    // e.g: 17:54	東京・大塚の護国寺着。中曽根康弘元総理大臣の通夜に参列
    val result = line.strip().split("\\s+", 2).toSeq

    val hourMinutes     = result(0).split(":").map(n => n.toInt).toSeq
    val (hour, minutes) = (hourMinutes(0), hourMinutes(1))

    val pDate = getPreviousDateInformation()
    val month = pDate.getMonthValue
    val day   = pDate.getDayOfMonth

    val zdt = ZonedDateTime.of(this.year, month, day, hour, minutes, 0, 0, jst)
    val row = PrimeMinisterAction(action = result(1), actionDate = zdt)
    row
  }

  private def getPreviousDateInformation(): ZonedDateTime = {
    this.previousDate
  }

  private def saveDateInformation(action: PrimeMinisterAction): Unit = {
    this.previousDate = action.actionDate
  }

  def parser(line: String): Option[PrimeMinisterAction] = {
    val actions = line match {
      case s if baseFormat.findFirstIn(s).isDefined => {
        val actionString = generalParser(s)
        Some(actionString)
      }
      case s if onlyDateFormat.findFirstIn(s).isDefined => {
        val actionString = onlyDateParser(s)
        Some(actionString)
      }
      case s if onlyTimeFormat.findFirstIn(s).isDefined => {
        val actionString = onlyTimeParser(s)
        Some(actionString)
      }
      case _ => {
        //            println("WARN => " + line)
        None
      }
    }
    if (actions.isDefined) {
      saveDateInformation(actions.get)
    }
    actions
  }
}
