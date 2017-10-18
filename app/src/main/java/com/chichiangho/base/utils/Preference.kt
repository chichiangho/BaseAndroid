package com.chichiangho.base.utils

import android.content.Context
import com.chichiangho.base.base.BaseApplication
import com.chichiangho.base.base.ProjectDefines
import com.chichiangho.base.bean.User
import com.chichiangho.base.extentions.toJson
import com.chichiangho.base.extentions.toObj

object Preference {
    var loginUser: User? = null
        get() = field ?: (get(ProjectDefines.PREFERENCE_KEY_USER_INFO, "") as String).toObj(User::class.java)
        set(value) {
            field = value
            set(ProjectDefines.PREFERENCE_KEY_USER_INFO, value?.toJson() ?: "")
        }

    private fun set(key: String, data: Any, asynchronous: Boolean = true) {
        val type = data.javaClass.simpleName
        val sharedPreferences = BaseApplication.appContext
                .getSharedPreferences(ProjectDefines.PREFERENCE_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        when (type) {
            "Integer" -> editor.putInt(key, data as Int)
            "Boolean" -> editor.putBoolean(key, data as Boolean)
            "String" -> editor.putString(key, data as String)
            "Float" -> editor.putFloat(key, data as Float)
            "Long" -> editor.putLong(key, data as Long)
        }

        if (asynchronous)
            editor.apply()//异步方式写入磁盘，如果对缓存数据正确性,实时保存要求很高应使用commit()
        else
            editor.commit()
    }

    private fun get(key: String, defValue: Any): Any {
        val type = defValue.javaClass.simpleName
        val sharedPreferences = BaseApplication.appContext.getSharedPreferences(ProjectDefines.PREFERENCE_NAME, Context.MODE_PRIVATE)

        return when (type) {
            "Integer" -> sharedPreferences.getInt(key, defValue as Int)
            "Boolean" -> sharedPreferences.getBoolean(key, defValue as Boolean)
            "String" -> sharedPreferences.getString(key, defValue as String)
            "Float" -> sharedPreferences.getFloat(key, defValue as Float)
            "Long" -> sharedPreferences.getLong(key, defValue as Long)
            else -> defValue
        }
    }
}