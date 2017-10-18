package com.chichiangho.base.wxapi

import android.content.Intent
import android.os.Bundle

import com.chichiangho.base.base.BaseActivity
import com.chichiangho.base.extentions.toastShort
import com.chichiangho.base.pay.PayUtil
import com.tencent.mm.opensdk.constants.ConstantsAPI
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler
import com.tencent.mm.opensdk.openapi.WXAPIFactory

/**
 * 该类目录必须在 包名.wxapi 目录下，否则将不回调
 * Created by chichiangho on 2017/9/7.
 */

class WXPayEntryActivity : BaseActivity(), IWXAPIEventHandler {
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val wxapi = WXAPIFactory.createWXAPI(this, PayUtil.wechat_app_id)
        wxapi.handleIntent(intent, this)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        val wxapi = WXAPIFactory.createWXAPI(this, PayUtil.wechat_app_id)
        wxapi.handleIntent(intent, this)
    }

    override fun onReq(baseReq: BaseReq) {}

    override fun onResp(baseResp: BaseResp) {
        if (baseResp.type == ConstantsAPI.COMMAND_PAY_BY_WX) {
            when (baseResp.errCode) {
                BaseResp.ErrCode.ERR_OK -> finish()
                else -> {
                    toastShort("${baseResp.errCode}")
                    finish()
                }
            }
        }
    }
}

