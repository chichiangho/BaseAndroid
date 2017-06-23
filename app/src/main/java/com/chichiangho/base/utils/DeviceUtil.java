package com.chichiangho.base.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.WindowManager;

import com.chichiangho.base.base.BaseApplication;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class DeviceUtil {
    private static DeviceUtil instance = null;
    private String mMacAddr;
    private String mPhoneNum;
    private String mAppName;
    private int mAppVersion = 0;
    private String mAppVersionName = "0";
    private String mDeviceType;
    private String mDeviceName;
    private String mOsVersion;
    private static int screenWidth;
    private static int screenHeight;

    private static int dpToPx(Resources res, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                res.getDisplayMetrics());
    }

    public static int getScreenWidth() {
        return screenWidth;
    }

    public static int getScreenHeight() {
        return screenHeight;
    }

    public static synchronized DeviceUtil getInstance() {
        if (instance == null)
            instance = new DeviceUtil();
        return instance;
    }

    public void init(Context ctx) {
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo info = pm.getPackageInfo(ctx.getPackageName(), 0);
            mAppVersionName = info.versionName;
            mAppVersion = info.versionCode;
            mAppName = (String) pm.getApplicationLabel(info.applicationInfo);


            DisplayMetrics metrics = new DisplayMetrics();
            WindowManager wm = (WindowManager) BaseApplication.getInstance().getSystemService(Context.WINDOW_SERVICE);
            wm.getDefaultDisplay().getMetrics(metrics);
            screenWidth = metrics.widthPixels;
            screenHeight = metrics.heightPixels;
        } catch (Exception e) {
            e.printStackTrace();
        }

        tryRefreshMacAddr(ctx);

        mOsVersion = android.os.Build.VERSION.RELEASE;
        mDeviceName = android.os.Build.BRAND;
        mDeviceType = android.os.Build.MODEL;
    }

    public void tryRefreshMacAddr(Context ctx) {
        if (mMacAddr != null && !mMacAddr.isEmpty()) {
            return;
        }

        try {
            WifiManager wifiManager = (WifiManager) ctx.getSystemService(Context.WIFI_SERVICE);

            if (wifiManager.isWifiEnabled()) {
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                if (wifiInfo != null)
                    mMacAddr = wifiInfo.getMacAddress();
            }

            if (mMacAddr == null || mMacAddr.isEmpty()) {
                mMacAddr = getMacAddrWithCmd();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getMacAddr() {
        return mMacAddr;
    }

    public String getPhoneNum() {
        return mPhoneNum;
    }

    public void setPhoneNum(String mPhoneNum) {
        this.mPhoneNum = mPhoneNum;
    }

    public String getAppName() {
        return mAppName;
    }

    public int getAppVersion() {
        return mAppVersion;
    }

    public String getAppVersionName() {
        return mAppVersionName;
    }

    public String getDeviceType() {
        return mDeviceType;
    }

    public String getDeviceName() {
        return mDeviceName;
    }

    public String getOsVersion() {
        return mOsVersion;
    }

    private String getMacAddrWithCmd() {
        String result = "";
        String Mac = "";
        result = callCmd("ifconfig", "HWaddr");

        // 如果返回的result == null，则说明网络不可取
        if (result == null) {
            return null;
        }

        // 对该行数据进行解析
        // 例如：eth0 Link encap:Ethernet HWaddr 00:16:E8:3E:DF:67
        if (result.length() > 0 && result.contains("HWaddr") == true) {
            Mac = result.substring(result.indexOf("HWaddr") + 6, result.length() - 1);

            if (Mac.length() > 1) {
                Mac = Mac.replaceAll(" ", "");
                result = "";
                String[] tmp = Mac.split(":");
                for (int i = 0; i < tmp.length; ++i) {
                    result += tmp[i];
                }
            }
        }
        return result;
    }

    private String callCmd(String cmd, String filter) {
        String result = "";
        String line = "";
        try {
            Process proc = Runtime.getRuntime().exec(cmd);
            InputStreamReader is = new InputStreamReader(proc.getInputStream());
            BufferedReader br = new BufferedReader(is);

            // 执行命令cmd，只取结果中含有filter的这一行
            while ((line = br.readLine()) != null && line.contains(filter) == false) {

            }

            result = line;
            Logger.i("test", "result: " + result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
