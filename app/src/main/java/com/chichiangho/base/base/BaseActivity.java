package com.chichiangho.base.base;

import android.support.v7.app.AppCompatActivity;

import com.chichiangho.base.widgets.LoadingDialog;

public class BaseActivity extends AppCompatActivity {
    private LoadingDialog loadingDialog;

    protected void showLoading(String info) {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(this, info);
        }
        loadingDialog.setMsg(info);
        loadingDialog.show();
    }

    protected void hideLoading() {
        if (loadingDialog != null) {
            loadingDialog.cancel();
        }
        loadingDialog = null;
    }
}
