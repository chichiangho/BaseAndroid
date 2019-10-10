package com.chichiangho.common.extentions

import android.arch.lifecycle.LifecycleOwner
import com.uber.autodispose.AutoDispose
import com.uber.autodispose.ObservableSubscribeProxy
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import io.reactivex.Emitter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

fun <T> Emitter<T>.onNextComplete(t: T) {
    onNext(t)
    onComplete()
}

fun <T> Observable<T>.io_main(): Observable<T> =
        subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

fun <T> Observable<T>.autoDispose(owner: LifecycleOwner): ObservableSubscribeProxy<T> =
        `as`(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(owner)))

fun <T> Observable<T>.autoDispose_io_main(owner: LifecycleOwner): ObservableSubscribeProxy<T> =
        io_main().autoDispose(owner)

fun <T> Observable<ArrayList<T>>.flat(): Observable<T> = flatMap { Observable.fromIterable(it) }

fun delayThenRunOnUiThread(millisecond: Long, action: () -> Unit): Disposable =
        Observable.timer(millisecond, TimeUnit.MILLISECONDS).io_main().subscribe { action.invoke() }

fun <T> rxDoInBackground(action: () -> T): Observable<T> =
        Observable.create<T> {
            try {
                it.onNext(action.invoke())
                it.onComplete()
            } catch (e: Exception) {
                it.onError(e)
            }
        }.subscribeOn(Schedulers.io())

fun <T> rxRunOnUiThread(action: () -> T): Observable<T> =
        Observable.create<T> {
            try {
                it.onNext(action.invoke())
                it.onComplete()
            } catch (e: Exception) {
                it.onError(e)
            }
        }.subscribeOn(AndroidSchedulers.mainThread())

fun <T, R> Observable<T>.rxRunOnUiThread(action: (it: T) -> R): Observable<R> =
        observeOn(AndroidSchedulers.mainThread()).map { action.invoke(it) }

fun <T, R> Observable<T>.rxDoInBackground(action: (it: T) -> R): Observable<R> =
        observeOn(Schedulers.io()).map { action.invoke(it) }

fun <T> Observable<T>.commit(): Disposable = subscribe { }
