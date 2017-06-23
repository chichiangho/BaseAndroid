package com.chichiangho.base.widgets;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chichiangho.base.R;

public class LoadingDialog extends Dialog {
    private TextView tv;
    private String msg;

    public LoadingDialog(Context context, String msg) {
        super(context, R.style.loadingDialogStyle);
        this.msg = msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
    private LoadingDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loading);
        tv = (TextView)this.findViewById(R.id.tv);
        tv.setText(msg);
        LinearLayout linearLayout = (LinearLayout)this.findViewById(R.id.LinearLayout);
        linearLayout.getBackground().setAlpha(210);
    }
}
