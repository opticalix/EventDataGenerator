package com.opticalix.common.utils;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @author Felix
 * @date 09/03/2019 10:36 AM
 * @email opticalix@gmail.com
 */
public class IOUtils {
    public static Properties readProperties(String path) {
        if (path == null || !new File(path).exists()) {
            return null;
        }
        Properties properties = new Properties();
        FileInputStream in = null;
        try {
            in = new FileInputStream(path);
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            silentClose(in);
        }
        return properties;
    }

    public static void silentClose(Closeable stream) {
        if (stream == null) {
            return;
        }
        try {
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
