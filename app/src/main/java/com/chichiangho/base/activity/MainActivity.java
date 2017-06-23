package com.chichiangho.base.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import com.chichiangho.base.R;
import com.chichiangho.base.base.BaseTabActivity;
import com.chichiangho.base.fragment.HomeFragment;


/**
 * Created by chichiangho on 2017/3/23.
 */

public class MainActivity extends BaseTabActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addTab(new HomeFragment(), R.string.home)
                .addTab(new HomeFragment(), R.string.descovery)
                .addTab(new HomeFragment(), R.string.mine).commit();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, 12345);
        }
    }

    @Override
    protected void onViewPagerSelected(int arg0) {
        super.onViewPagerSelected(arg0);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //确保要请求的权限已在manifest声明，否则返回数组中该权限将为PackageManager.PERMISSION_DENIED
        if (requestCode == 12345 && !(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
            new AlertDialog.Builder(this).setCancelable(false).setMessage("很抱歉，APP正常运行需要您授予相关权限")
                    .setPositiveButton("关闭", (dialog, which) -> finish())
                    .create().show();
        }
    }
}
