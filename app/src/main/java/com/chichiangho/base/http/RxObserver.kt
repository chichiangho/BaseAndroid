package com.chichiangho.base.http

import io.reactivex.Observer

/**
 * Created by chichiangho on 2017/7/5.
 */

abstract class RxObserver<T> : Observer<T> {

    override fun onError(e: Throwable) {

    }

    override fun onComplete() {

    }
}
