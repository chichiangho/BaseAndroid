package com.chichiangho.base.http

import com.chichiangho.base.base.BaseResponse
import com.chichiangho.base.utils.ToastUtil

import io.reactivex.Observer
import io.reactivex.disposables.Disposable

/**
 * Created by chichiangho on 2017/4/27.
 */

abstract class RxHttpObserver<T> : Observer<T> {

    override fun onSubscribe(d: Disposable) {}

    override fun onNext(value: T) {
        onSuccess(value)//错误码解析在HandleResponse
    }

    override fun onError(e: Throwable) {
        if (e.message != null) {
            when (e.message) {
                BaseResponse.CODE_TOKEN_TIMEOUT -> ToastUtil.showShort("token过期请重新登录")
                else -> {
                    if (e.message?.contains("Failed to connect") ?: false) {
                        ToastUtil.showShort("网络异常")
                    } else if (e.message != "") {
                        ToastUtil.showShort(e.message as String)
                    }
                }
            }
        }

        onFailed()
        onComplete()
    }

    protected abstract fun onSuccess(response: T)

    protected fun onFailed() {}

    override fun onComplete() {}
}
