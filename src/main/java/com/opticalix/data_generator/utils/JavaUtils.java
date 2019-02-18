package com.opticalix.data_generator.utils;

/**
 * @author Felix
 * @date 14/02/2019 7:35 PM
 * @email opticalix@gmail.com
 */
public class JavaUtils {
    public static String formatSize(long v) {
        if (v < 1024) return v + " B";
        int z = (63 - Long.numberOfLeadingZeros(v)) / 10;
        return String.format("%.1f %sB", (double) v / (1L << (z * 10)), " KMGTPE".charAt(z));
    }

    public static void printHeapSize() {
        long heapSize = Runtime.getRuntime().totalMemory();

        // Get maximum size of heap in bytes. The heap cannot grow beyond this size.// Any attempt will result in an OutOfMemoryException.
        long heapMaxSize = Runtime.getRuntime().maxMemory();

        // Get amount of free memory within the heap in bytes. This size will increase // after garbage collection and decrease as new objects are created.
        long heapFreeSize = Runtime.getRuntime().freeMemory();

        System.out.println("heapSize " + formatSize(heapSize));
        System.out.println("heapMaxSize " + formatSize(heapMaxSize));
        System.out.println("heapFreeSize " + formatSize(heapFreeSize));
    }
}
