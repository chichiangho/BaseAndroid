package com.chichiangho.base.http

import com.chichiangho.base.base.BaseResponse
import com.chichiangho.base.extentions.toastShort
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import java.net.SocketTimeoutException

/**
 * Created by chichiangho on 2017/4/27.
 */

abstract class RxHttpObserver<T> : Observer<T> {

    override fun onSubscribe(d: Disposable) {}

    override fun onNext(value: T) {
        onSuccess(value)//错误码解析在HandleResponse
    }

    override fun onError(e: Throwable) {
        e.message?.let {
            when (e.message) {
                BaseResponse.CODE_TOKEN_TIMEOUT -> toastShort("token过期请重新登录")
                else -> {
                    if (e.message?.contains("Failed to connect") == true) {
                        toastShort("网络异常")
                    } else if (e.message != "") {
                        toastShort(e.message as String)
                    }
                }
            }
        } ?: let {
            if (e is SocketTimeoutException)
                toastShort("time out")
        }

        onFailed()
        onComplete()
    }

    protected abstract fun onSuccess(response: T)

    protected fun onFailed() {}

    override fun onComplete() {}
}
