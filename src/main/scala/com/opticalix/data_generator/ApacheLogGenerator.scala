package com.opticalix.data_generator

import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.time.{Duration, LocalDate, LocalDateTime, ZoneId}
import java.util.concurrent.ThreadLocalRandom

import scala.util.Random

/**
  * 64.242.88.10 - - [07/Mar/2004:16:10:02 -0800] "GET /mailman/listinfo/hsdivision HTTP/1.1" 200 6291
  */
object ApacheLogGenerator {

  def provideSingleIP(): String = {
    val ips = Array("ip1", "ip2")
    val random = new Random()
    ips(random.nextInt(ips.length))
  }

  def provideSingleFormatTime(fmt: String, startTime: LocalDateTime, maxDuration: Long): String = {
    val randomMillis = ThreadLocalRandom.current().nextLong(maxDuration)
    val randomLocalDateTime = startTime.plusSeconds(randomMillis / 1000)
    val zoneId = ZoneId.systemDefault()
    randomLocalDateTime.atZone(zoneId).format(DateTimeFormatter.ofPattern(fmt))
  }

  def provideSingleCurrDayFormatTime(fmt: String): String = {
    val currDate = LocalDate.now()
    val zoneId = ZoneId.systemDefault()
    val dayMillis = Duration.ofDays(1).get(ChronoUnit.SECONDS).toInt * 1000
    val todayStartTime = LocalDateTime.of(currDate.getYear,currDate.getMonthValue,currDate.getDayOfMonth, 0, 0)
    val randomMillis = new Random().nextInt(dayMillis)
    val randomLocalDateTime = todayStartTime.plusSeconds(randomMillis / 1000)
    randomLocalDateTime.atZone(zoneId).format(DateTimeFormatter.ofPattern(fmt))
  }

  def provideHttpInfo(): String = {
    val protocol = "HTTP/1.1"
    val methods = Array("GET", "POST", "DELETE", "PUT")
    val hosts = Array("/mailman/listinfo/hsdivision")

    val method = methods(new Random().nextInt(methods.length))
    val host = hosts(new Random().nextInt(hosts.length))
    s"$method $host $protocol"
  }
}
