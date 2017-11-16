package com.chichiangho.base.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.chichiangho.base.widgets.LoadingDialog
import io.reactivex.disposables.Disposable
import java.util.*

abstract class BaseActivity : AppCompatActivity() {
    private lateinit var loadingDialog: LoadingDialog
    protected var disposables = ArrayList<Disposable>()

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
        disposables.filter { disposable -> !disposable.isDisposed }.forEach { disposable -> disposable.dispose() }
    }
}
