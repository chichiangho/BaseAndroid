package com.chichiangho.common.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import io.reactivex.disposables.CompositeDisposable

abstract class BaseActivity : AppCompatActivity() {
    private lateinit var loadingDialog: LoadingDialog
    protected var disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadingDialog = LoadingDialog(this)
    }

    protected fun showLoading(info: String) {
        loadingDialog.setMsg(info)
        if (!isFinishing)
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
