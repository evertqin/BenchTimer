package com.benchtimer.utils;

/**
 * Created by evert on 3/9/14.
 */
public class Utils {
    public static String formatTimeIntoDisplay(long enteredTime) {
        long second = enteredTime % 100;
        long minute = enteredTime / 100 % 100;
        long hour = enteredTime / 10000;

        String displayString = String.format(
                "%02d:%02d:%02d", hour, minute, second);
        return displayString;
    }
}
