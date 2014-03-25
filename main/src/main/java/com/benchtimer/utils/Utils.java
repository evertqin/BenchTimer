package com.benchtimer.utils;

import android.util.Log;

import java.util.concurrent.TimeUnit;

/**
 * Created by evert on 3/9/14.
 */
public class Utils {
    public static String formatTimeIntoDisplay(long enteredTime) {
        long hours = TimeUnit.MILLISECONDS.toHours(enteredTime);
        enteredTime -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(enteredTime);
        enteredTime -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(enteredTime);
        enteredTime -= TimeUnit.SECONDS.toMillis(seconds);

        return String.format("%02d:%02d:%02d.%02d", hours, minutes, seconds,enteredTime / 10);

    }

    /**
     * @param timeStr
     * @return time in millisecond
     */
    public static long convertToTime(String timeStr) throws IllegalArgumentException{
        Log.i("Utils", "In convertToTime timeStr: " + timeStr);
        String[] fields = timeStr.split(":");
        if (fields.length == 3) {
            long time = Long.valueOf(fields[0]) * 3600 +  Long.valueOf(fields[1]) * 60 + Long.valueOf(fields[2]);
            time *= 1000;
            return time;
        } else {
            throw new IllegalArgumentException("Cannot convert time " + timeStr);
        }

    }
}
