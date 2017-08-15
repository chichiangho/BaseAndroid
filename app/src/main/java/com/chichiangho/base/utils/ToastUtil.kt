package com.chichiangho.base.utils

import android.os.Looper
import android.widget.Toast

import com.chichiangho.base.base.BaseApplication

/**
 * Created by chichiangho on 2017/3/24.
 */

object ToastUtil {

    fun showShort(str: String) {
        if (Looper.getMainLooper() === Looper.myLooper())
            Toast.makeText(BaseApplication.getInstance(), str, Toast.LENGTH_SHORT).show()
    }

    fun showLong(str: String) {
        if (Looper.getMainLooper() === Looper.myLooper())
            Toast.makeText(BaseApplication.getInstance(), str, Toast.LENGTH_LONG).show()
    }
}
