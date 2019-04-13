package com.opticalix.common.pojo;

import com.opticalix.common.utils.TimeUtils;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * @author Felix
 * @date 02/01/2019 4:17 PM
 * @email opticalix@gmail.com
 */
public class ManufactureEvent implements Serializable {
    public static final String PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSSXXX";
    public long timestamp;
    public int mf01;
    public int mf02;
    public int mf03;

    public ManufactureEvent() {
    }

    public ManufactureEvent(long timestamp, int mf01, int mf02, int mf03) {
        this.timestamp = timestamp;
        this.mf01 = mf01;
        this.mf02 = mf02;
        this.mf03 = mf03;
    }

    @Override
    public String toString() {
        return "ManufactureEvent{" +
                "fmt_time=" + TimeUtils.formatTime(timestamp, PATTERN) +
                ", timestamp=" + timestamp +
                ", mf01=" + mf01 +
                ", mf02=" + mf02 +
                ", mf03=" + mf03 +
                '}';
    }

    public static ManufactureEvent convert2ManufactureEvent(String rawLine) {
        String[] split = rawLine.split("\t");
        String utc = split[0];
        LocalDateTime ldt = LocalDateTime.parse(utc, DateTimeFormatter.ofPattern(PATTERN));
        return new ManufactureEvent(ldt.toInstant(ZoneOffset.of("+8")).toEpochMilli(), Integer.valueOf(split[2]), Integer.valueOf(split[3]), Integer.valueOf(split[4]));
    }
}
