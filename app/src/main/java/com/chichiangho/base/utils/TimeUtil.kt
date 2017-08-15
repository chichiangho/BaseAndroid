package com.chichiangho.base.utils

import java.text.SimpleDateFormat
import java.util.*

object TimeUtil {

    /**
     * format time string ,accurate to seconds

     * @param millionSeconds
     * *
     * @return
     */
    fun formatTimeAccurateSec(millionSeconds: Long): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val time = dateFormat.format(Date(millionSeconds))
        return time
    }

    fun formatTimeAccurateSec(date: Date): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val time = dateFormat.format(date)
        return time
    }

    /**
     * format time string ,accurate to minutes

     * @param millionSeconds
     * *
     * @return
     */
    fun formatTimeAccurateMin(millionSeconds: Long): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm")
        val time = dateFormat.format(Date(millionSeconds))
        return time
    }

    fun formatTimeAccurateMin(date: Date): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm")
        val time = dateFormat.format(date)
        return time
    }

    fun formatTimeAccurateDay(millionSeconds: Long): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val time = dateFormat.format(Date(millionSeconds))
        return time
    }

    fun formatTimeAccurateDay(date: Date): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val time = dateFormat.format(date)
        return time
    }

    fun formatHourMinute(millionSeconds: Long): String {
        val dateFormat = SimpleDateFormat("HH:mm")
        val time = dateFormat.format(Date(millionSeconds))
        return time
    }

    fun formatHourMinute(date: Date): String {
        val dateFormat = SimpleDateFormat("HH:mm")
        val time = dateFormat.format(date)
        return time
    }

    /**
     * format xxx天xx小时xx分

     * @param millionSeconds
     * *
     * @return
     */
    fun formatDayHourMin(millionSeconds: Long): String {
        val day = millionSeconds / (24 * 60 * 60 * 1000)
        val hour = millionSeconds % (24 * 60 * 60 * 1000) / (60 * 60 * 1000)
        val min = millionSeconds % (60 * 60 * 1000) / (60 * 1000)

        return "" + (if (day == 0L) "" else day.toString() + "天") + if (hour == 0L) "" else hour.toString() + "小时" + if (min == 0L) "" else min.toString() + "分"
    }
}
