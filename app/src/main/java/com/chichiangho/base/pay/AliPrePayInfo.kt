package com.chichiangho.base.pay

import java.io.Serializable

/**
 * Created by chichiangho on 2017/3/31.
 */

class AliPrePayInfo : Serializable {
    var service = ""
    var partner = ""
    var _input_charset = ""
    var sign_type = ""
    var sign = ""
    var notify_url = ""
    var app_id = ""
    var appenv = ""
    var out_trade_no = ""
    var subject = ""
    var payment_type = ""
    var seller_id = ""
    var total_fee = ""
    var body = ""
    var goods_type = ""
    var hb_fq_param = ""
    var rn_check = ""
    var it_b_pay = ""
    var extern_token = ""
    var promo_params = ""
    var extend_params = ""

    override fun toString(): String {
        return "service=" + service +
                "&partner=" + partner +
                "&_input_charset=" + _input_charset +
                "&sign_type=" + sign_type +
                "&sign=" + sign +
                "&notify_url=" + notify_url +
                "&out_trade_no=" + out_trade_no +
                "&subject=" + subject +
                "&payment_type=" + payment_type +
                "&seller_id=" + seller_id +
                "&total_fee=" + total_fee +
                "&body=" + body +
                "&app_id=" + app_id +
                "&appenv=" + appenv +
                "&goods_type=" + goods_type +
                "&hb_fq_param=" + hb_fq_param +
                "&rn_check=" + rn_check +
                "&it_b_pay=" + it_b_pay +
                "&extern_token=" + extern_token +
                "&promo_params=" + promo_params +
                "&extend_params=" + extend_params
    }
}
