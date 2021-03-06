package com.chichiangho.base.base

import java.io.Serializable


class BaseResponse : Serializable {
    var code = ""
    var msg = ""
    var reqUrl = ""

    companion object {
        val CODE_SUCCESS = "1000" // 返回数据成功
        val CODE_TOKEN_TIMEOUT = "1007" // 用户Token过期，需要重新登录
        val CODE_REPEATED_REQUEST = "REPEATED_REQUEST"
    }
}
