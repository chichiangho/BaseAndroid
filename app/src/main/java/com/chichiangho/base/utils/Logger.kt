package com.chichiangho.base.utils

import android.os.Environment
import android.util.Log
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException

object Logger {

    private val ENABLE_LOG_TO_FILE: Boolean = true
    var logLevel = LogLevel.DEBUG // 日志的等级，可以进行配置，最好在Application中进行全局的配置

    enum class LogLevel {
        ERROR {
            override val value: Int get() = 0
        },
        WARN {
            override val value: Int get() = 1
        },
        INFO {
            override val value: Int get() = 2
        },
        DEBUG {
            override val value: Int get() = 3
        };

        abstract val value: Int
    }

    private val MIN_STACK_OFFSET = 3
    private val TOP_LEFT_CORNER = '┌'
    private val BOTTOM_LEFT_CORNER = '└'
    private val MIDDLE_CORNER = '├'
    private val SINGLE_DIVIDER = "────────────────────────────────────────────"
    const val JSON_SPLIT = "||||"
    val TOP_BORDER = TOP_LEFT_CORNER + SINGLE_DIVIDER + SINGLE_DIVIDER
    val BOTTOM_BORDER = BOTTOM_LEFT_CORNER + SINGLE_DIVIDER + SINGLE_DIVIDER
    val MIDDLE_BORDER = MIDDLE_CORNER + SINGLE_DIVIDER + SINGLE_DIVIDER
    val JSON_INDENT = 2

    @JvmStatic fun e(TAG: String, msg: String) {
        if (LogLevel.ERROR.value <= logLevel.value) {
            if (msg.isNotBlank()) {
                val s = getMethodNames()
                Log.e(TAG, String.format(s, msg))
            }
        }
    }

    @JvmStatic fun w(TAG: String, msg: String) {
        if (LogLevel.WARN.value <= logLevel.value) {
            if (msg.isNotBlank()) {
                val s = getMethodNames()
                Log.e(TAG, String.format(s, msg))
            }
        }
    }

    @JvmStatic fun i(TAG: String, msg: String) {
        if (LogLevel.INFO.value <= logLevel.value) {
            if (msg.isNotBlank()) {
                val s = getMethodNames()
                Log.i(TAG, String.format(s, msg))
            }
        }
    }

    @JvmStatic fun d(TAG: String, msg: String) {
        if (LogLevel.DEBUG.value <= logLevel.value) {
            if (msg.isNotBlank()) {
                val s = getMethodNames()
                Log.d(TAG, String.format(s, msg))
            }
        }
    }

    @JvmStatic fun json(TAG: String, json: String) {
        var json = json
        if (json.isBlank()) {
            d(TAG, "Empty/Null json content")
            return
        }

        try {
            val jsonSplit = json.split(JSON_SPLIT).toTypedArray()
            var pre = ""
            if (jsonSplit.size < 2) {
                json = jsonSplit[0]
            } else {
                pre = jsonSplit[0]
                json = jsonSplit[1]
            }

            if (json.startsWith("{")) {
                val jsonObject = JSONObject(json)
                json = (if (pre.isEmpty()) "" else pre + "\n") + jsonObject.toString(JSON_INDENT)
                json = json.replace("\n".toRegex(), "\n| ")
                d(TAG, json)
                return
            }
            if (json.startsWith("[")) {
                val jsonArray = JSONArray(json)
                json = (if (pre.isEmpty()) "" else pre + "\n") + jsonArray.toString(JSON_INDENT)
                json = json.replace("\n".toRegex(), "\n| ")
                d(TAG, json)
                return
            }
            e(TAG, "Invalid Json")
        } catch (e: JSONException) {
            e(TAG, "Invalid Json")
        }
    }

    private fun getMethodNames(): String {
        val sElements = Thread.currentThread().stackTrace
        var stackOffset = getStackOffset(sElements)
        stackOffset++
        val builder = StringBuilder()
        builder.append(TOP_BORDER)
                .append("\r\n") // 添加当前线程名
                .append("| " + "Thread: " + Thread.currentThread().name)
                .append("\r\n")
                .append(MIDDLE_BORDER)
                .append("\r\n") // 添加类名、方法名、行数
                .append("| ")
                .append(sElements[stackOffset].className)
                .append(".")
                .append(sElements[stackOffset].methodName)
                .append(" ")
                .append(" (")
                .append(sElements[stackOffset].fileName)
                .append(":").append(sElements[stackOffset].lineNumber)
                .append(")")
                .append("\r\n")
                .append(MIDDLE_BORDER)
                .append("\r\n") // 添加打印的日志信息
                .append("| ")
                .append("%s")
                .append("\r\n")
                .append(BOTTOM_BORDER)
                .append("\r\n")
        return builder.toString()
    }

    fun getStackOffset(trace: Array<StackTraceElement>): Int {
        var i = MIN_STACK_OFFSET
        while (i < trace.size) {
            val e = trace[i]
            val name = e.className
            if (name != Logger::class.java.name) {
                return --i
            }
            i++
        }
        return -1
    }

    @JvmStatic fun logToFile(time: String, msg: Any?, fileName: String) {
        if (null == msg) {
            return
        }
        if (ENABLE_LOG_TO_FILE) {
            writeToSDCard(fileName, "[" + time + "] " + msg.toString() + "\n")
        }
    }

    private fun writeToSDCard(fileName: String, text: String) {
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            var fileOutputStream: FileOutputStream? = null
            val fileInputStream: FileInputStream? = null
            try {
                val file = File(Environment.getExternalStorageDirectory(), fileName)
                fileOutputStream = FileOutputStream(file, true)
                fileOutputStream.write(text.toByteArray())
            } catch (e: Exception) {

            } finally {
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                }
                if (fileInputStream != null) {
                    try {
                        fileInputStream.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                }
            }
        }
    }
}
