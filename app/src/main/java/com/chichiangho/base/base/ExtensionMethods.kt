package com.chichiangho.base.base

import io.reactivex.Emitter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by chichiangho on 2017/8/11.
 */

fun <T> Emitter<T>.onNextComplete(t: T) {
    onNext(t)
    onComplete()
}

fun <T> Observable<T>.io_main(): Observable<T> {
    return subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
}