package com.chichiangho.base.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.chichiangho.base.base.BaseApplication;
import com.chichiangho.base.base.ProjectDefines;
import com.chichiangho.base.bean.User;

public class PreferenceUtil {

    private static User user;

    public static User getUser() {
        if (user == null) {
            String userJson = (String) get(ProjectDefines.PREFERENCE_KEY_USER_INFO, "");
            if (!userJson.equals(""))
                return JsonUtil.fromJson(userJson, User.class);
        }
        return user;
    }

    public static void setUser(User user) {
        if (user == null)
            return;

        PreferenceUtil.user = user;
        set(ProjectDefines.PREFERENCE_KEY_USER_INFO, JsonUtil.toJson(user));
    }

    public static void set(String key, Object data) {
        set(key, data, true);
    }

    public static void set(String key, Object data, boolean asynchronous) {
        String type = data.getClass().getSimpleName();
        SharedPreferences sharedPreferences = BaseApplication.getInstance()
                .getSharedPreferences(ProjectDefines.PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if ("Integer".equals(type)) {
            editor.putInt(key, (Integer) data);
        } else if ("Boolean".equals(type)) {
            editor.putBoolean(key, (Boolean) data);
        } else if ("String".equals(type)) {
            editor.putString(key, (String) data);
        } else if ("Float".equals(type)) {
            editor.putFloat(key, (Float) data);
        } else if ("Long".equals(type)) {
            editor.putLong(key, (Long) data);
        }

        if (asynchronous)
            editor.apply();//异步方式写入磁盘，如果对缓存数据正确性,实时保存要求很高应使用commit()
        else
            editor.commit();
    }

    public static Object get(String key, Object defValue) {
        String type = defValue.getClass().getSimpleName();
        SharedPreferences sharedPreferences = BaseApplication.getInstance().getSharedPreferences
                (ProjectDefines.PREFERENCE_NAME, Context.MODE_PRIVATE);

        if ("Integer".equals(type)) {
            return sharedPreferences.getInt(key, (Integer) defValue);
        } else if ("Boolean".equals(type)) {
            return sharedPreferences.getBoolean(key, (Boolean) defValue);
        } else if ("String".equals(type)) {
            return sharedPreferences.getString(key, (String) defValue);
        } else if ("Float".equals(type)) {
            return sharedPreferences.getFloat(key, (Float) defValue);
        } else if ("Long".equals(type)) {
            return sharedPreferences.getLong(key, (Long) defValue);
        }

        return defValue;
    }

}
