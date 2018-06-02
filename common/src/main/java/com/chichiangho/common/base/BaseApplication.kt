package com.chichiangho.common.base

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import java.lang.ref.WeakReference

class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext



        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityPaused(activity: Activity) {
            }

            override fun onActivityResumed(activity: Activity) {
            }

            override fun onActivityStarted(activity: Activity) {
            }

            override fun onActivityDestroyed(activity: Activity) {
                lifeCycleListenerMap[activity]?.get("onDestroy")?.forEach { it() }
                lifeCycleListenerMap.remove(activity)
            }

            override fun onActivitySaveInstanceState(activity: Activity, bundle: Bundle?) {
            }

            override fun onActivityStopped(activity: Activity) {
            }

            override fun onActivityCreated(activity: Activity, bundle: Bundle?) {
                topActivity = WeakReference(activity)
            }
        })
    }

    companion object {
        lateinit var appContext: Context
        lateinit var topActivity: WeakReference<Activity>
        val lifeCycleListenerMap = HashMap<Activity, HashMap<String, ArrayList<() -> Unit>>>()
    }
}
