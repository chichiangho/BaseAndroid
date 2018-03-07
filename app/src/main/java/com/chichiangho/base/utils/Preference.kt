package com.chichiangho.base.utils

import android.content.Context
import com.chichiangho.base.base.AppConfigs
import com.chichiangho.base.bean.User
import com.chichiangho.common.base.BaseApplication
import com.chichiangho.common.extentions.toJson
import com.chichiangho.common.extentions.toObj

object Preference {
    var loginUser: User? = null
        get() = field ?: (get(AppConfigs.PREFERENCE_KEY_USER_INFO, "") as String).toObj(User::class.java)
        set(value) {
            field = value
            set(AppConfigs.PREFERENCE_KEY_USER_INFO, value?.toJson() ?: "")
        }

    private fun set(key: String, defValue: Any, asynchronous: Boolean = true) {
        val sharedPreferences = BaseApplication.appContext
                .getSharedPreferences(AppConfigs.PREFERENCE_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        when (defValue.javaClass.simpleName) {
            "Integer" -> editor.putInt(key, defValue as Int)
            "Boolean" -> editor.putBoolean(key, defValue as Boolean)
            "String" -> editor.putString(key, defValue as String)
            "Float" -> editor.putFloat(key, defValue as Float)
            "Long" -> editor.putLong(key, defValue as Long)
        }

        if (asynchronous)
            editor.apply()//异步方式写入磁盘，如果对缓存数据正确性,实时保存要求很高应使用commit()
        else
            editor.commit()
    }

    private fun get(key: String, defValue: Any): Any {
        val sharedPreferences = BaseApplication.appContext.getSharedPreferences(AppConfigs.PREFERENCE_NAME, Context.MODE_PRIVATE)

        return when (defValue.javaClass.simpleName) {
            "Integer" -> sharedPreferences.getInt(key, defValue as Int)
            "Boolean" -> sharedPreferences.getBoolean(key, defValue as Boolean)
            "String" -> sharedPreferences.getString(key, defValue as String)
            "Float" -> sharedPreferences.getFloat(key, defValue as Float)
            "Long" -> sharedPreferences.getLong(key, defValue as Long)
            else -> defValue
        }
    }
}