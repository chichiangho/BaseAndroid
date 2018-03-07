/*
 * Copyright (C) 2017 chichiangho
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.chichiangho.base.database

import android.database.sqlite.SQLiteDatabase
import android.os.Environment
import com.chichiangho.base.bean.CityArea
import com.chichiangho.common.base.BaseApplication
import java.io.*
import java.util.*

/**
 * Created by chichiangho on 2017/3/16.
 */

object CityDBManager {

    private val TAG = "CityDBManager"

    private val ASSETS_NAME = "china_cities.db"
    private val DB_NAME = "china_cities.db"
    private val TABLE_NAME = "city"
    private val NAME = "name"
    private val PINYIN = "pinyin"
    private val BUFFER_SIZE = 1024


    private val dbPath: String
        get() =
            File.separator + "data"+Environment.getDataDirectory().absolutePath + File.separator+ BaseApplication.appContext.packageName + File.separator + "databases" + File.separator

    fun copyDBFile() {
        val dir = File(dbPath)
        if (!dir.exists()) {
            dir.mkdirs()
        }
        val dbFile = File(dbPath + DB_NAME)
        if (!dbFile.exists()) {
            val `is`: InputStream
            val os: OutputStream
            try {
                `is` = BaseApplication.appContext.resources.assets.open(ASSETS_NAME)
                os = FileOutputStream(dbFile)
                val buffer = ByteArray(BUFFER_SIZE)
                var length: Int
                while (true) {
                    length = `is`.read(buffer, 0, buffer.size)
                    if (length <= 0)
                        break
                    os.write(buffer, 0, length)
                }
                os.flush()
                os.close()
                `is`.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }

    /**
     * 读取所有城市

     * @return
     */
    val allCities: List<CityArea>
        get() {
            val DB_PATH = dbPath
            val db = SQLiteDatabase.openOrCreateDatabase(DB_PATH + DB_NAME, null)
            val cursor = db.rawQuery("select * from " + TABLE_NAME, null)
            val result = ArrayList<CityArea>()
            var city: CityArea
            while (cursor.moveToNext()) {
                val name = cursor.getString(cursor.getColumnIndex(NAME))
                val pinyin = cursor.getString(cursor.getColumnIndex(PINYIN))
                city = CityArea(name, pinyin)
                result.add(city)
            }
            cursor.close()
            db.close()
            Collections.sort(result, CityComparator())
            return result
        }

    /**
     * 通过名字或者拼音搜索

     * @param keyword
     * *
     * @return
     */
    fun searchCity(keyword: String): List<CityArea> {
        val DB_PATH = dbPath
        val db = SQLiteDatabase.openOrCreateDatabase(DB_PATH + DB_NAME, null)
        val cursor = db.rawQuery("select * from " + TABLE_NAME + " where name like \"%" + keyword
                + "%\" or pinyin like \"%" + keyword + "%\"", null)
        val result = ArrayList<CityArea>()
        var city: CityArea
        while (cursor.moveToNext()) {
            val name = cursor.getString(cursor.getColumnIndex(NAME))
            val pinyin = cursor.getString(cursor.getColumnIndex(PINYIN))
            city = CityArea(name, pinyin)
            result.add(city)
        }
        cursor.close()
        db.close()
        Collections.sort(result, CityComparator())
        return result
    }

    /**
     * a-z排序
     */
    private class CityComparator : Comparator<CityArea> {
        override fun compare(lhs: CityArea, rhs: CityArea): Int {
            val a = lhs.pinyin!!.substring(0, 1)
            val b = rhs.pinyin!!.substring(0, 1)
            return a.compareTo(b)
        }
    }
}
