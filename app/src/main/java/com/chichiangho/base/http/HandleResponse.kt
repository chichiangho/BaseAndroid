package com.chichiangho.base.http

import com.chichiangho.base.base.BaseResponse

import io.reactivex.functions.Function

/**
 * Created by chichiangho on 2017/4/28.
 */

class HandleResponse<T> : Function<T, T> {

    @Throws(Exception::class)
    override fun apply(t: T): T {
        if (t is BaseResponse) {//错误逻辑处理在RxObserver
            val response = t as BaseResponse
            when (response.code) {
                BaseResponse.CODE_SUCCESS -> { }
                BaseResponse.CODE_TOKEN_TIMEOUT -> throw Exception(response.code)//因为要调用UI线程，交由onError处理
                else -> throw Exception(response.msg)
            }
        }
        return t
    }
}
