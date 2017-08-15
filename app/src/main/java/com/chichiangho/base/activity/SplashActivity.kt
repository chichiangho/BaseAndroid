package com.chichiangho.base.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle

import com.chichiangho.base.base.CrashHandler
import com.chichiangho.base.utils.DeviceUtil
import com.chichiangho.base.utils.PreferenceUtil

/**
 * Created by chichiangho on 2017/5/18.
 */

class SplashActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        doInitThings()

        if (PreferenceUtil.getUser() != null) {
            startActivity(Intent(this, DailianActivity::class.java))
        } else {
            startActivity(Intent(this, DailianActivity::class.java))
        }
        finish()
    }

    private fun doInitThings() {
        CrashHandler().init()
        DeviceUtil.init(this)
        //        OkHttpClientManager.setCertificates(getAssets().open("证书"));
    }
}
