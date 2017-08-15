package com.chichiangho.base.utils

import android.content.Context
import com.chichiangho.base.base.BaseApplication
import com.chichiangho.base.base.ProjectDefines
import com.chichiangho.base.bean.User

object PreferenceUtil {

    private var user: User? = null

    fun getUser(): User? {
        if (user == null) {
            val userJson = get(ProjectDefines.PREFERENCE_KEY_USER_INFO, "") as String
            if (userJson != "")
                return JsonUtil.fromJson(userJson, User::class.java)
        }
        return user
    }

    fun setUser(user: User?) {
        if (user == null)
            return

        PreferenceUtil.user = user
        set(ProjectDefines.PREFERENCE_KEY_USER_INFO, JsonUtil.toJson(user))
    }

    @JvmOverloads fun set(key: String, data: Any, asynchronous: Boolean = true) {
        val type = data.javaClass.simpleName
        val sharedPreferences = BaseApplication.getInstance()
                .getSharedPreferences(ProjectDefines.PREFERENCE_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        if ("Integer" == type) {
            editor.putInt(key, data as Int)
        } else if ("Boolean" == type) {
            editor.putBoolean(key, data as Boolean)
        } else if ("String" == type) {
            editor.putString(key, data as String)
        } else if ("Float" == type) {
            editor.putFloat(key, data as Float)
        } else if ("Long" == type) {
            editor.putLong(key, data as Long)
        }

        if (asynchronous)
            editor.apply()//异步方式写入磁盘，如果对缓存数据正确性,实时保存要求很高应使用commit()
        else
            editor.commit()
    }

    operator fun get(key: String, defValue: Any): Any {
        val type = defValue.javaClass.simpleName
        val sharedPreferences = BaseApplication.getInstance().getSharedPreferences(ProjectDefines.PREFERENCE_NAME, Context.MODE_PRIVATE)

        if ("Integer" == type) {
            return sharedPreferences.getInt(key, defValue as Int)
        } else if ("Boolean" == type) {
            return sharedPreferences.getBoolean(key, defValue as Boolean)
        } else if ("String" == type) {
            return sharedPreferences.getString(key, defValue as String)
        } else if ("Float" == type) {
            return sharedPreferences.getFloat(key, defValue as Float)
        } else if ("Long" == type) {
            return sharedPreferences.getLong(key, defValue as Long)
        }

        return defValue
    }

}
