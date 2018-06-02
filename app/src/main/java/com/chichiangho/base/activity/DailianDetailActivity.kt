package com.chichiangho.base.activity

import android.os.Bundle
import com.chichiangho.common.base.BaseTitleActivity
import com.chichiangho.common.extentions.showLoading

/**
 * Created by chichiangho on 2017/7/14.
 */

class DailianDetailActivity : BaseTitleActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showLoading()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
