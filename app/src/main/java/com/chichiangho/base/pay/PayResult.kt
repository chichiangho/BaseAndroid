package com.chichiangho.base.pay

/**
 * Created by chichiangho on 2017/3/31.
 */

class PayResult(rawResult: String?) {

    var resultStatus: Int = 0
        private set
    var result: String? = null
        private set
    var memo = ""

    init {//支付宝
        rawResult?.let {
            val resultParams = rawResult.split(";")
            for (resultParam in resultParams) {
                if (resultParam.startsWith("resultStatus=")) {
                    resultStatus = Integer.valueOf(gatValue(resultParam, "resultStatus"))
                }
                if (resultParam.startsWith("result=")) {
                    result = gatValue(resultParam, "result")
                }
                if (resultParam.startsWith("memo=")) {
                    memo = gatValue(resultParam, "memo")
                }
            }
        }
    }

    private fun gatValue(content: String, key: String): String {
        val prefix = key + "={"
        return content.substring(content.indexOf(prefix) + prefix.length, content.lastIndexOf("}"))
    }

    companion object {
        //支付宝
        @JvmField
        val PAY_OK_STATUS = 9000// 支付成功
        @JvmField
        val PAY_WAIT_CONFIRM_STATUS = 8000// 交易待确认
        @JvmField
        val PAY_FAILED_STATUS = 4000// 交易失败
        @JvmField
        val PAY_CANCLE_STATUS = 6001// 交易取消
        @JvmField
        val PAY_NET_ERR_STATUS = 6002// 网络出错
        @JvmField
        val PAY_UNKNOWN_ERR_STATUS = 6004// 结果未知

        //微信
        @JvmField
        val WECHAT_PAY_RESULT_ACTION = "com.tencent.mm.opensdk.WECHAT_PAY_RESULT_ACTION"
        @JvmField
        val WECHAT_PAY_RESULT_EXTRA = "com.tencent.mm.opensdk.WECHAT_PAY_RESULT_EXTRA"
        @JvmField
        val WECHAT_NOT_INSTALL = "微信未安装"//自定义，非服务器错误码
        @JvmField
        val WECHAT_UNSUPPORT_ERR = "微信版本不支持"//自定义，非服务器错误码
        @JvmField
        val SUCCESS = 0
        @JvmField
        val FAILED = -1
        @JvmField
        val USER_CANCEL = -2
    }
}
