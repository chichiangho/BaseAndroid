package com.chichiangho.base.utils

import android.content.Context
import android.content.Context.TELEPHONY_SERVICE
import android.content.res.Resources
import android.telephony.TelephonyManager
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.WindowManager


object DeviceUtil {
    @JvmStatic var phoneNum = ""
        private set
    @JvmStatic var appName = ""
        private set
    @JvmStatic var appVersion = 0
        private set
    @JvmStatic var appVersionName = "0"
        private set
    @JvmStatic var deviceType = ""
        private set
    @JvmStatic var deviceName = ""
        private set
    @JvmStatic var osVersion = ""
        private set
    @JvmStatic var screenWidth = 0
        private set
    @JvmStatic var screenHeight = 0
        private set
    @JvmStatic var deviceNum = ""
        private set

    @JvmStatic fun init(ctx: Context) {
        try {
            val pm = ctx.packageManager
            val info = pm.getPackageInfo(ctx.packageName, 0)
            appVersionName = info.versionName
            appVersion = info.versionCode
            appName = pm.getApplicationLabel(info.applicationInfo) as String


            val metrics = DisplayMetrics()
            val wm = ctx.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            wm.defaultDisplay.getMetrics(metrics)
            screenWidth = metrics.widthPixels
            screenHeight = metrics.heightPixels

            val telephonyMgr = ctx.getSystemService(TELEPHONY_SERVICE) as TelephonyManager
            deviceNum = telephonyMgr.deviceId
            phoneNum = telephonyMgr.line1Number
        } catch (e: Exception) {
            e.printStackTrace()
        }

        osVersion = android.os.Build.VERSION.RELEASE
        deviceName = android.os.Build.BRAND
        deviceType = android.os.Build.MODEL
    }

    @JvmStatic fun dpToPx(res: Resources, dp: Int): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(),
                res.displayMetrics).toInt()
    }
}
