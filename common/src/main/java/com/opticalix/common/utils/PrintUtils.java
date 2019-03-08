package com.opticalix.common.utils;

import java.io.PrintStream;
import java.util.Locale;
import java.util.Map;

/**
 * @author Felix
 * @date 06/01/2019 10:02 PM
 * @email opticalix@gmail.com
 */
public class PrintUtils {

    private static PrintStream sErr = System.err;

    public static void printMap(Map map) {
        sErr.println(map.toString());
    }

    public static void printFmt(String fmt, Object... args) {
        sErr.println(String.format(Locale.CHINA, fmt, args));
    }
}
