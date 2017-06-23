package com.chichiangho.base.utils;

import android.os.Environment;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 封装原有的日志信息，通过配置控制不同类型的日志输出。
 */
public final class Logger {

    private static final boolean IS_VERBOSE = true;
    public static final boolean IS_DEBUG = true;
    private static final boolean IS_INFO = true;
    private static final boolean IS_WARN = true;
    private static final boolean IS_ERROR = true;
    private static final boolean ENABLE_LOGTO_FILE = false;
    private static final String PREFIX = "--->";

    public static void v(String tag, String msg) {
        if (IS_VERBOSE) {
            StackTraceElement targetStackTraceElement = getTargetStackTraceElement();
            Log.v(tag, PREFIX + msg + "(" + targetStackTraceElement.getFileName() + ":"
                    + targetStackTraceElement.getLineNumber() + ")");
        }
    }

    public static void debug(String tag, String msg) {
        if (IS_DEBUG) {
            Log.d(tag, PREFIX + msg);
        }
    }

    public static void d(String tag, String msg) {
        if (IS_DEBUG) {
            StackTraceElement targetStackTraceElement = getTargetStackTraceElement();
            Log.d(tag, PREFIX + msg + "(" + targetStackTraceElement.getFileName() + ":"
                    + targetStackTraceElement.getLineNumber() + ")");
        }
    }

    public static void i(String tag, String msg) {
        if (IS_INFO) {
            StackTraceElement targetStackTraceElement = getTargetStackTraceElement();
            Log.i(tag, PREFIX + msg + "(" + targetStackTraceElement.getFileName() + ":"
                    + targetStackTraceElement.getLineNumber() + ")");
        }
    }

    public static void w(String tag, String msg) {
        if (IS_WARN) {
            StackTraceElement targetStackTraceElement = getTargetStackTraceElement();
            Log.w(tag, PREFIX + msg + "(" + targetStackTraceElement.getFileName() + ":"
                    + targetStackTraceElement.getLineNumber() + ")");
        }
    }

    public static void e(String tag, String msg) {
        if (IS_ERROR) {
            StackTraceElement targetStackTraceElement = getTargetStackTraceElement();
            Log.e(tag, PREFIX + msg + "(" + targetStackTraceElement.getFileName() + ":"
                    + targetStackTraceElement.getLineNumber() + ")");
        }
    }

    public static void json(String tag, String msg) {
        if (IS_DEBUG) {
            getJson(tag, msg);
        }
    }

    /**
     * Print log but print 3000 characters at most in every line.
     *
     * @param tag
     * @param msg
     */
    public static void printLog(String tag, String msg) {
        if (IS_DEBUG && msg != null) {
            int count = msg.length();
            int lineCount = 3000;
            for (int i = 0; i < count; i = i + lineCount) {
                if (i + lineCount <= count) {
                    Log.d(tag, msg.substring(i, i + lineCount));
                } else {
                    Log.d(tag, msg.substring(i, count));
                }
            }
        }
    }

    /**
     * Print log but print 3000 characters at most in every line.
     *
     * @param tag
     * @param msg
     */
    public static void printLog2(String tag, String msg) {
        if (IS_DEBUG && msg != null) {
            int count = msg.length();
            int lineCount = 3000;
            for (int i = 0; i < count; i = i + lineCount) {
                if (i + lineCount <= count) {
                    Log.d(tag, msg.substring(i, i + lineCount));
                } else {
                    Log.d(tag, msg.substring(i, count));
                }
            }
        }
    }

    public static void logToFile(String time, Object msg, String fileName) {
        if (null == msg) {
            return;
        }
        if (ENABLE_LOGTO_FILE) {
            writeToSDCard(fileName, "[" + time + "] " + msg.toString() + "\n");
        }
    }

    public static void cleanLogFile(String fileName) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            try {
                File file = new File(Environment.getExternalStorageDirectory(), fileName);
                if (file.exists()) {
                    file.delete();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void writeToSDCard(String fileName, String text) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            FileOutputStream fileOutputStream = null;
            FileInputStream fileInputStream = null;
            try {
                File file = new File(Environment.getExternalStorageDirectory(), fileName);
                fileOutputStream = new FileOutputStream(file, true);
                fileOutputStream.write(text.getBytes());
            } catch (Exception e) {

            } finally {
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fileInputStream != null) {
                    try {
                        fileInputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private static StackTraceElement getTargetStackTraceElement() {
        StackTraceElement targetStackTrace = null;
        boolean shouldTrace = false;
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (StackTraceElement stackTraceElement : stackTrace) {
            boolean isLogMethod = stackTraceElement.getClassName().equals(Logger.class.getName());
            if (shouldTrace && !isLogMethod) {
                targetStackTrace = stackTraceElement;
                break;
            }
            shouldTrace = isLogMethod;
        }
        return targetStackTrace;
    }

    private static String getJson(String tag, String jsonStr) {
        if (jsonStr == null || jsonStr.isEmpty()) {
            Logger.debug(tag, "Empty/Null json content");
            return "Empty/Null json content";
        }
        try {
            jsonStr = jsonStr.trim();
            if (jsonStr.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(jsonStr);
                Logger.debug(tag, jsonObject.toString(2));
                return jsonObject.toString(2);
            }
            if (jsonStr.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(jsonStr);
                Logger.debug(tag, jsonArray.toString(2));
                return jsonArray.toString(2);
            }
            Logger.debug(tag, "Empty/Null json content");
            return "Invalid Json, Please Check: " + jsonStr;
        } catch (JSONException e) {
            e.printStackTrace();
            Logger.debug(tag, "Empty/Null json content");
            return "Invalid Json, Please Check: " + jsonStr;
        }
    }
}
