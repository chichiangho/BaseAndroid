package com.chichiangho.base.base;

import android.support.v4.app.Fragment;

import com.chichiangho.base.widgets.LoadingDialog;

/**
 * Created by chichiangho on 2017/6/6.
 */

public class BaseFragment extends Fragment {
    private LoadingDialog loadingDialog;

    protected void showLoading(String info) {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(getActivity(), info);
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
