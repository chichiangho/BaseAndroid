package com.chichiangho.base.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {

    /**
     * format time string ,accurate to seconds
     *
     * @param millionSeconds
     * @return
     */
    public static String formatTimeAccurateSec(long millionSeconds) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = dateFormat.format(new Date(millionSeconds));
        return time;
    }

    /**
     * format time string ,accurate to minutes
     *
     * @param millionSeconds
     * @return
     */
    public static String formatTimeAccurateMin(long millionSeconds) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String time = dateFormat.format(new Date(millionSeconds));
        return time;
    }

    /**
     * format xxx天xx小时xx分
     *
     * @param millionSeconds
     * @return
     */
    public static String formatDayHourMin(long millionSeconds) {
        long day = millionSeconds / (24 * 60 * 60 * 1000);
        long hour = (millionSeconds % (24 * 60 * 60 * 1000)) / (60 * 60 * 1000);
        long min = (millionSeconds % (60 * 60 * 1000)) / (60 * 1000);

        return "" + (day == 0 ? "" : (day + "天")) + (hour == 0 ? "" : (hour + "小时") + (min == 0 ? "" : (min + "分")));
    }
}
