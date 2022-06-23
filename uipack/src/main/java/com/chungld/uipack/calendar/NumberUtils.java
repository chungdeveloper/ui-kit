package com.chungld.uipack.calendar;

import java.text.NumberFormat;

/**
 * Created by Le Duc Chung on 2019-10-10
 * Time created: 9:17 AM
 * Project: Flight
 * Coding for Life
 */
public class NumberUtils {

    public static String formatCurrency(String value) {
        return NumberFormat.getNumberInstance().format(convertStringToLong(value));
    }

    public static String formatCurrency(long value) {
        return NumberFormat.getNumberInstance().format((value));
    }

    public static String formatCurrency(double price) {
        return NumberFormat.getNumberInstance().format((round(price)));
    }

    public static long convertStringToLong(String value) {
        try {
            return Long.parseLong(value);
        } catch (Exception ignored) {
        }
        return 0;
    }

    public static int convertStringToInteger(String value) {
        try {
            return Integer.parseInt(value);
        } catch (Exception ignored) {
        }
        return 0;
    }

    public static long round(double maxPrice) {
        return Math.round(maxPrice);
    }

    public static int round(float maxPrice) {
        return Math.round(maxPrice);
    }
}
