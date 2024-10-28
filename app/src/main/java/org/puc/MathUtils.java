package org.puc;

public class MathUtils {
    public static int abs(int value) {
        return value < 0 ? -value : value;
    }

    public static double abs(double value) {
        return value < 0 ? -value : value;
    }

    public static double floor(double value) {
        int truncated = (int) value;
        return value < truncated ? truncated - 1 : truncated;
    }

}
