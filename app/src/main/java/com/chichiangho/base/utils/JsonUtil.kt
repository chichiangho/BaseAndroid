package com.chichiangho.base.utils

import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.google.gson.JsonSyntaxException
import java.util.*

object JsonUtil {
    private var instance: GsonBuilder? = null

    @Synchronized private fun build(): GsonBuilder {
        if (instance == null) {
            instance = GsonBuilder()
        }
        return instance as GsonBuilder
    }

    private fun getInstance(): GsonBuilder {
        return build()
    }

    fun toJson(obj: Any): String {
        return getInstance().create().toJson(obj)
    }

    @Throws(JsonSyntaxException::class)
    fun <T> fromJson(json: String, clazz: Class<T>): T {
        return getInstance().create().fromJson(json, clazz)
    }

    fun <T> fromJsonArray(jsonArray: String, clz: Class<T>): ArrayList<T> {
        val array = JsonParser().parse(jsonArray).asJsonArray
        val mList = array.mapTo(ArrayList<T>()) { getInstance().create().fromJson(it, clz) }
        return mList
    }

    fun <T> getListFromJSON(json: String, type: Class<Array<T>>): List<T> {
        val list = getInstance().create().fromJson(json, type)
        return Arrays.asList(*list)
    }

}
