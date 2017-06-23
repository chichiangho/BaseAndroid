package com.chichiangho.base.utils;

import android.widget.Toast;

import com.chichiangho.base.base.BaseApplication;

/**
 * Created by chichiangho on 2017/3/24.
 */

public class ToastUtil {

    public static void showShort(String str) {
        Toast.makeText(BaseApplication.getInstance(), str, Toast.LENGTH_SHORT).show();
    }

    public static void showLong(String str) {
        Toast.makeText(BaseApplication.getInstance(), str, Toast.LENGTH_LONG).show();
    }
}
