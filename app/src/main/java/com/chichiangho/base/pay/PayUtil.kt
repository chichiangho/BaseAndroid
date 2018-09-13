package com.chichiangho.base.pay

import android.app.Activity
import com.alipay.sdk.app.PayTask
import com.tencent.mm.opensdk.openapi.WXAPIFactory

/**
 * Created by chichiangho on 2017/3/31.
 */

class PayUtil(private var activity: Activity?) {
    private lateinit var prePayInfo: PrePayInfo

    fun setPrePayInfo(prePayInfo: PrePayInfo): PayUtil {
        this.prePayInfo = prePayInfo
        return this
    }

    fun pay(payListener: PayListener) {
        when {
            prePayInfo.ali != null -> {
                val task = PayTask(activity)
                val resultMap = task.pay(prePayInfo.ali?.toString() ?: "", true)
                val result = PayResult(resultMap)
                when (result.resultStatus) {
                    PayResult.PAY_OK_STATUS -> payListener.onSuccess()
                    else -> payListener.onFailed(result.memo)
                }
            }
            prePayInfo.weChat != null -> {
                val wxapi = WXAPIFactory.createWXAPI(activity, wechat_app_id)
                wxapi.registerApp(wechat_app_id)

                if (!wxapi.isWXAppInstalled) {
                    payListener.onFailed(PayResult.WECHAT_NOT_INSTALL)
                    return
                }

//                if (!wxapi.isWXAppSupportAPI) {
//                    payListener.onFailed(PayResult.WECHAT_UNSUPPORT_ERR)
//                    return
//                }

                // 发送支付请求：跳转到微信客户端，支付结果在WXPayEntryActivity中获取并处理
                wxapi.sendReq(prePayInfo.weChat)
            }
            prePayInfo.union != null -> {
            }
        }
    }

    interface PayListener {
        fun onSuccess()

        fun onFailed(memo: String)
    }

    companion object {
        @JvmField
        var wechat_app_id = ""
    }
}
