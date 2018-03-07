package com.chichiangho.common.base

import android.app.Application
import android.content.Context

class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        //不要在application中做太多事，否则将导致启动app时出现白屏等情况，可将一些初始化操作放在splash界面的onCreate中，以给用户更好的呈现效果
    }

    companion object {
        lateinit var appContext: Context
    }
}
