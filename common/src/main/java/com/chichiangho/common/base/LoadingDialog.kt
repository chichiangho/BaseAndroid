package com.chichiangho.common.base

import android.app.Dialog
import android.content.Context
import com.changhong.common.R
import kotlinx.android.synthetic.main.dialog_loading.*

class LoadingDialog(context: Context?) : Dialog(context, R.style.loadingDialogStyle) {

    fun setMsg(msg: String) {
        tv.text = msg
    }

    init {
        setContentView(R.layout.dialog_loading)
        LinearLayout.background.alpha = 210
    }
}
