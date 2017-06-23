package com.chichiangho.base.base;

import android.app.Application;
import android.content.Context;

public class BaseApplication extends Application {
    static BaseApplication instance = null;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        //不要在application中做太多事，否则将导致启动app时出现白屏等情况，可将一些初始化操作放在splash界面的onCreate中，以给用户更好的呈现效果
    }

    public static Context getInstance() {
        return instance;
    }
}
