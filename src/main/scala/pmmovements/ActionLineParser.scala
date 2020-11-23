package pmmovements

import java.time.{ZoneId, ZonedDateTime}

import scala.util.matching.Regex

trait IFParser {
  def parser(action: String): Option[PrimeMinisterAction]
}

class ActionLineParser(year: Int) extends IFParser {
  private val baseFormat: Regex     = "\\d{1,2}/\\d{1,2}\\s+\\d{1,2}:\\d{1,2}\\s+.*$".r
  private val onlyDateFormat: Regex = "\\d{1,2}/\\d{1,2}\t+.*$".r // "\\d{1,2}/\\d{1,2}\t+.*$".r.findFirstIn(line)
  private val onlyTimeFormat: Regex = "\\d{1,2}:\\d{1,2}\t+.*$".r

  private val jst = ZoneId.of("Asia/Tokyo")

  // Current state
  var previousDate: ZonedDateTime = null

  private def parseDate(monthAndDate: String): (Int, Int) = {
    val monthDate = monthAndDate.split("/").map(n => n.toInt).toSeq
    (monthDate(0), monthDate(1))
  }

  private def parseTime(timeStr: String): (Int, Int) = {
    val hourMinutes = timeStr.split(":").map(n => n.toInt).toSeq
    (hourMinutes(0), hourMinutes(1))
  }

  private def generalParser(line: String): PrimeMinisterAction = {
    // e.g: 4/30	1:35	(日本時間午前）政府専用機でＵＡＥ＝アラブ首長国連邦のアブダビ国際空港着
    val result = line.strip().split("\\s+", 3).toSeq

    val (month, day)    = parseDate(result(0))
    val (hour, minutes) = parseTime(result(1))

    val zdt = ZonedDateTime.of(this.year, month, day, hour, minutes, 0, 0, jst)
    val row = PrimeMinisterAction(action = result(2), actionDate = zdt)
    row
  }

  private def onlyDateParser(line: String): PrimeMinisterAction = {
    // e.g: 4/30	(日本時間午前）政府専用機でＵＡＥ＝アラブ首長国連邦のアブダビ国際空港着
    val result = line.strip().split("\\s+", 2).toSeq

    val (month, day) = parseDate(result(0))

    val zdt = ZonedDateTime.of(this.year, month, day, 9, 0, 0, 0, jst)
    val row = PrimeMinisterAction(action = result(1), actionDate = zdt)
    row
  }

  private def onlyTimeParser(line: String): PrimeMinisterAction = {
    // e.g: 17:54	東京・大塚の護国寺着。中曽根康弘元総理大臣の通夜に参列
    val result = line.strip().split("\\s+", 2).toSeq

    val (hour, minutes) = parseTime(result(0))

    val month = getPreviousDate().getMonthValue
    val day   = getPreviousDate().getDayOfMonth

    val zdt = ZonedDateTime.of(this.year, month, day, hour, minutes, 0, 0, jst)
    val row = PrimeMinisterAction(action = result(1), actionDate = zdt)
    row
  }

  private def getPreviousDate(): ZonedDateTime = {
    this.previousDate
  }

  private def savePreviousDate(action: PrimeMinisterAction): Unit = {
    this.previousDate = action.actionDate
  }

  def parser(line: String): Option[PrimeMinisterAction] = {
    val result = line match {
      case s if baseFormat.findFirstIn(s).isDefined => {
        val action = generalParser(s)
        Some(action)
      }
      case s if onlyDateFormat.findFirstIn(s).isDefined => {
        val action = onlyDateParser(s)
        Some(action)
      }
      case s if onlyTimeFormat.findFirstIn(s).isDefined => {
        val action = onlyTimeParser(s)
        Some(action)
      }
      case _ => {
        //            println("WARN => " + line)
        None
      }
    }
    if (result.isDefined) {
      savePreviousDate(result.get)
    }
    result
  }
}
