package com.chichiangho.base.base;

import com.chichiangho.base.utils.DeviceUtil;
import com.chichiangho.base.utils.Logger;

import java.sql.Timestamp;

/**
 * Created by chichiangho on 2017/4/18.
 */

public class CrashHandler implements Thread.UncaughtExceptionHandler {

    public void init() {
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 当UncaughtException发生时会回调该函数来处理
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        DeviceUtil device = DeviceUtil.getInstance();
        StringBuilder builder = new StringBuilder("\nBrand: " + device.getDeviceName()
                + ", Model: " + device.getDeviceType() + ", OS: " + "Android "
                + device.getOsVersion() + ", Version: " + device.getAppVersionName() + "\n");
        builder.append(ex.toString() + "\n");
        builder.append(ex.getLocalizedMessage() + "\n");
        StackTraceElement[] stack = ex.getStackTrace();
        for (StackTraceElement element : stack) {
            builder.append(element.toString() + "\n");
        }
        Logger.e("CrashHandler", "系统异常退出" + builder.toString());
        Throwable throwable = ex.getCause();
        while (null != throwable) {
            StackTraceElement[] currentStack = throwable.getStackTrace();
            for (StackTraceElement element : currentStack) {
                builder.append(element.toString() + "\n");
            }
            throwable = throwable.getCause();
        }
        Logger.logToFile(new Timestamp(System.currentTimeMillis()).toString(), builder,
                "com_changhong_park_pda_crash.log");

        final String log = builder.toString();

//        RxHttpDataProvider.upLoadCrashLog(log, new RxHttpObserver<BaseResponse>() {
//            @Override
//            public void onStart() {
//
//            }
//
//            @Override
//            public void onSuccess(BaseResponse response) {
//
//            }
//
//            @Override
//            public void onFailed() {
//
//            }
//
//            @Override
//            public void onComplete() {
        android.os.Process.killProcess(android.os.Process.myPid());
//            }
//        });
    }
}
