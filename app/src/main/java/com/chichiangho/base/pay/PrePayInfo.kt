package com.chichiangho.base.pay

import com.tencent.mm.opensdk.modelpay.PayReq

/**
 * Created by chichiangho on 2017/9/7.
 */
class PrePayInfo {
    var ali: AliPrePayInfo? = null
    var weChat: PayReq? = null
    var union: UnionPrePayInfo? = null
}