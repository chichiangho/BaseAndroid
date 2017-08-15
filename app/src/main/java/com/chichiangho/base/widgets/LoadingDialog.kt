package com.chichiangho.base.widgets

import android.app.Dialog
import android.content.Context
import com.chichiangho.base.R
import kotlinx.android.synthetic.main.dialog_loading.*

class LoadingDialog : Dialog {

    constructor(context: Context?) : super(context, R.style.loadingDialogStyle) {
        setContentView(R.layout.dialog_loading)
        LinearLayout.background.alpha = 210
    }

    fun setMsg(msg: String) {
        tv.text = msg
    }
}
