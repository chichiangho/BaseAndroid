package com.chichiangho.base.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.chichiangho.base.base.CrashHandler;
import com.chichiangho.base.utils.DeviceUtil;
import com.chichiangho.base.utils.PreferenceUtil;

/**
 * Created by chichiangho on 2017/5/18.
 */

public class SplashActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        doInitThings();

        if (PreferenceUtil.getUser() != null) {
            startActivity(new Intent(this, MainActivity.class));
        } else {
            startActivity(new Intent(this, MainActivity.class));
        }
        finish();
    }

    private void doInitThings() {
        new CrashHandler().init();
        DeviceUtil.getInstance().init(this);
//        OkHttpClientManager.setCertificates(getAssets().open("证书"));
    }
}
