package com.chichiangho.base.utils;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JsonUtil {
    private static GsonBuilder instance;

    private static synchronized GsonBuilder build() {
        if (instance == null) {
            instance = new GsonBuilder();
        }
        return instance;
    }

    private static GsonBuilder getInstance() {
        return build();
    }

    public static String toJson(Object obj) {
        return getInstance().create().toJson(obj);
    }

    public static <T> T fromJson(String json, Class<T> clazz) throws JsonSyntaxException {
        return getInstance().create().fromJson(json, clazz);
    }

    public static <T> ArrayList<T> fromJsonArray(String jsonArray, Class<T> clz) {
        ArrayList<T> mList = new ArrayList<T>();
        JsonArray array = new JsonParser().parse(jsonArray).getAsJsonArray();
        for (final JsonElement elem : array) {
            mList.add(getInstance().create().fromJson(elem, clz));
        }
        return mList;
    }

    public static <T> List<T> getListFromJSON(String json, Class<T[]> type) {
        T[] list = getInstance().create().fromJson(json, type);
        return Arrays.asList(list);
    }

}
