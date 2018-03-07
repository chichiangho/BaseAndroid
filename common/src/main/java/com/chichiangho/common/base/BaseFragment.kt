package com.chichiangho.common.base

import android.os.Bundle
import android.support.v4.app.Fragment
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by chichiangho on 2017/6/6.
 */

abstract class BaseFragment : Fragment() {
    private lateinit var loadingDialog: LoadingDialog
    protected var disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadingDialog = LoadingDialog(context)
    }

    protected fun showLoading(info: String) {
        loadingDialog.setMsg(info)
        if (activity?.isFinishing == false)
            loadingDialog.show()
    }

    protected fun hideLoading() {
        loadingDialog.dismiss()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
    }
}
