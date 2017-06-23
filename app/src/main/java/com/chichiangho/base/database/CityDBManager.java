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
package com.chichiangho.base.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.chichiangho.base.base.BaseApplication;
import com.chichiangho.base.bean.CityArea;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by chichiangho on 2017/3/16.
 */

public class CityDBManager {

    private static final String TAG = "CityDBManager";

    private static final String ASSETS_NAME = "china_cities.db";
    private static final String DB_NAME = "china_cities.db";
    private static final String TABLE_NAME = "city";
    private static final String NAME = "name";
    private static final String PINYIN = "pinyin";
    private static final int BUFFER_SIZE = 1024;


    public static String getDbPath() {
        return File.separator + "data"
                + Environment.getDataDirectory().getAbsolutePath() + File.separator
                + BaseApplication.getInstance().getPackageName() + File.separator + "databases" + File.separator;

    }

    public static void copyDBFile() {
        String DB_PATH = getDbPath();
        File dir = new File(DB_PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File dbFile = new File(DB_PATH + DB_NAME);
        if (!dbFile.exists()) {
            InputStream is;
            OutputStream os;
            try {
                is = BaseApplication.getInstance().getResources().getAssets().open(ASSETS_NAME);
                os = new FileOutputStream(dbFile);
                byte[] buffer = new byte[BUFFER_SIZE];
                int length;
                while ((length = is.read(buffer, 0, buffer.length)) > 0) {
                    os.write(buffer, 0, length);
                }
                os.flush();
                os.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 读取所有城市
     *
     * @return
     */
    public static List<CityArea> getAllCities() {
        String DB_PATH = getDbPath();
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(DB_PATH + DB_NAME, null);
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME, null);
        List<CityArea> result = new ArrayList<>();
        CityArea city;
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(NAME));
            String pinyin = cursor.getString(cursor.getColumnIndex(PINYIN));
            city = new CityArea(name, pinyin);
            result.add(city);
        }
        cursor.close();
        db.close();
        Collections.sort(result, new CityComparator());
        return result;
    }

    /**
     * 通过名字或者拼音搜索
     *
     * @param keyword
     * @return
     */
    public static List<CityArea> searchCity(final String keyword) {
        String DB_PATH = getDbPath();
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(DB_PATH + DB_NAME, null);
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " where name like \"%" + keyword
                + "%\" or pinyin like \"%" + keyword + "%\"", null);
        List<CityArea> result = new ArrayList<>();
        CityArea city;
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(NAME));
            String pinyin = cursor.getString(cursor.getColumnIndex(PINYIN));
            city = new CityArea(name, pinyin);
            result.add(city);
        }
        cursor.close();
        db.close();
        Collections.sort(result, new CityComparator());
        return result;
    }

    /**
     * a-z排序
     */
    private static class CityComparator implements Comparator<CityArea> {
        @Override
        public int compare(CityArea lhs, CityArea rhs) {
            String a = lhs.getPinyin().substring(0, 1);
            String b = rhs.getPinyin().substring(0, 1);
            return a.compareTo(b);
        }
    }
}
