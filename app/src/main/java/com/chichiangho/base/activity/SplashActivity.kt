package com.chichiangho.base.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.chichiangho.base.utils.Preference
import com.chichiangho.common.base.CrashHandler

/**
 * Created by chichiangho on 2017/5/18.
 */

class SplashActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        doInitThings()

        if (Preference.loginUser != null) {
            startActivity(Intent(this, DailianActivity::class.java))
        } else {
            startActivity(Intent(this, DailianActivity::class.java))
        }
        finish()
    }

    private fun doInitThings() {
        CrashHandler().init()
        //        OkHttpClientManager.setCertificates(getAssets().open("证书"));
    }
}
