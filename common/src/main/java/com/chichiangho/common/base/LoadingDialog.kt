package com.chichiangho.common.base

import android.app.Activity
import android.app.Dialog
import android.content.Context
import com.changhong.common.R
import com.chichiangho.common.extentions.doOnDestroy
import kotlinx.android.synthetic.main.dialog_loading.*

class LoadingDialog private constructor(context: Context) : Dialog(context, R.style.loadingDialogStyle) {

    init {
        setContentView(R.layout.dialog_loading)
        LinearLayout.background.alpha = 210
    }

    fun setMsg(msg: String): LoadingDialog {
        tv.text = msg
        return this
    }

    companion object {
        private val map = HashMap<Activity, LoadingDialog?>()

        fun with(activity: Activity): LoadingDialog {
            return map[activity] ?: let {
                val dialog = LoadingDialog(activity)
                activity.doOnDestroy {
                    map.remove(activity)
                }
                map[activity] = dialog
                dialog
            }
        }

        fun dismiss(activity: Activity) {
            map[activity]?.dismiss()
        }
    }
}
