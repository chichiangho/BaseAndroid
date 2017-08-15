package com.chichiangho.base.base

import android.os.Bundle
import android.support.v4.app.Fragment
import com.chichiangho.base.widgets.LoadingDialog
import io.reactivex.disposables.Disposable
import java.util.*

/**
 * Created by chichiangho on 2017/6/6.
 */

open class BaseFragment : Fragment() {
    private lateinit var loadingDialog: LoadingDialog
    protected var disposables = ArrayList<Disposable>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadingDialog = LoadingDialog(context)
    }

    protected fun showLoading(info: String) {
        loadingDialog.setMsg(info)
        loadingDialog.show()
    }

    protected fun hideLoading() {
        loadingDialog.dismiss()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.filter { disposable -> !disposable.isDisposed }.forEach { disposable -> disposable.dispose() }
    }
}