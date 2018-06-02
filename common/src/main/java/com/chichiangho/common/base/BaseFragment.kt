package com.chichiangho.common.base

import android.os.Bundle
import android.support.v4.app.Fragment
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by chichiangho on 2017/6/6.
 */

abstract class BaseFragment : Fragment() {
    protected var disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
    }
}
