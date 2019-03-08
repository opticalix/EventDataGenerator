package com.opticalix.common.utils;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author Felix
 * @date 03/01/2019 7:27 PM
 * @email opticalix@gmail.com
 */
public class TimeUtils {
    public static String formatTime(long millis, String fmt) {
        SimpleDateFormat sdf = new SimpleDateFormat(fmt);
        return sdf.format(new Date(millis));
    }

    public static String formatTimeNewApi(long millis, String fmt) {
        return formatTimeNewApi(millis, fmt, ZoneId.systemDefault());
    }

    public static String formatTimeNewApi(long millis, String fmt, ZoneId zoneId) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), zoneId);
        return localDateTime.format(DateTimeFormatter.ofPattern(fmt));
    }
}
