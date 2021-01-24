import java.net.URI
import java.net.http.HttpClient.{Redirect, Version}
import java.net.http.{HttpClient, HttpRequest, HttpResponse}
import java.nio.charset.StandardCharsets
import java.nio.file.Files.newBufferedWriter
import java.nio.file.Paths
import java.time.format.DateTimeFormatter
import java.time.{Duration, ZoneId}
import scala.util.Using

object CurrentDate {

  @deprecated
  def downloadCSV(url: String): Option[String] = {
    // see: https://docs.oracle.com/javase/jp/11/docs/api/java.net.http/java/net/http/HttpClient.html
    val client = HttpClient
      .newBuilder()
      .version(Version.HTTP_1_1)
      .followRedirects(Redirect.NORMAL)
      .connectTimeout(Duration.ofSeconds(20))
      .build();

    val request = HttpRequest
      .newBuilder()
      .GET()
      .uri(URI.create(url))
      .timeout(Duration.ofSeconds(10))
      .build()
    val response = client.send(request, HttpResponse.BodyHandlers.ofString())
    response.statusCode() match {
      case 200 => {
        Option(response.body())
      }
      case _ => {
        None
      }
    }
  }

  def main(args: Array[String]): Unit = {
    val today        = java.time.LocalDate.now(ZoneId.of("Asia/Tokyo"))
    val yearAndMonth = today.format(DateTimeFormatter.ofPattern("YYYYMM"))

    val url      = s"https://www.nhk.or.jp/politics/souri/csv/${yearAndMonth}.csv"
    val response = scala.io.Source.fromURL(url)(StandardCharsets.UTF_16)

    Using(newBufferedWriter(Paths.get(s"original/${yearAndMonth}.csv"), StandardCharsets.UTF_8)) { writer =>
      response.mkString.split("\n").foreach(line => writer.write(line.stripTrailing() + "\n"))
    }
//    val writer = newBufferedWriter(Paths.get(s"original/${yearAndMonth}.csv"), StandardCharsets.UTF_8)
//    response.mkString.split("\n").foreach(line => writer.write(line.stripTrailing() + "\n"))
//    writer.close()
  }
}
