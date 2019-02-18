package com.opticalix.data_generator.utils

import java.time.format.DateTimeFormatter
import java.time.{Instant, LocalDateTime, ZoneId}

object Utils {
  def getStrFromArray(args: Array[String], target: String): Option[String] = {
    for (elem <- args) {
      if (elem.equals(target))
        elem
    }
    Some(null)
  }

  def formatTimeNewApi(millis: Long, fmt: String): String = formatTimeNewApi(millis, fmt, ZoneId.systemDefault)

  def formatTimeNewApi(millis: Long, fmt: String, zoneId: ZoneId): String = {
    val localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), zoneId)
    localDateTime.format(DateTimeFormatter.ofPattern(fmt))
  }

  def toNano(millis: Long): Long = {
    millis * 1000
  }

  def toMillis(nano: Long): Long = {
    nano / 1000
  }
}
