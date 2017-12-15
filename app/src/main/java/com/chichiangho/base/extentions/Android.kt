package com.chichiangho.base.extentions

import android.content.Context
import android.os.Looper
import android.util.TypedValue
import android.widget.Toast
import com.chichiangho.base.base.BaseApplication

val appCtx: Context
    get() = BaseApplication.appContext

fun toastShort(string: String?) {
    if (string == null) return
    if (Looper.getMainLooper() === Looper.myLooper())
        Toast.makeText(appCtx, string, Toast.LENGTH_SHORT).show()
}

fun toastLong(string: String?) {
    if (string == null) return
    if (Looper.getMainLooper() === Looper.myLooper())
        Toast.makeText(appCtx, string, Toast.LENGTH_LONG).show()
}

fun getDimension(id: Int) = appCtx.resources.getDimension(id).toInt()

fun dpToPx(dp: Int): Int =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), appCtx.resources.displayMetrics).toInt()


