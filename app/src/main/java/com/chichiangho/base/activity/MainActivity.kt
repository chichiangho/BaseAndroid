package com.chichiangho.base.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import com.chichiangho.base.R
import com.chichiangho.base.base.BaseTabActivity
import com.chichiangho.base.fragment.HomeFragment


/**
 * Created by chichiangho on 2017/3/23.
 */

class MainActivity : BaseTabActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addTab(HomeFragment(), R.string.home)
                .addTab(HomeFragment(), R.string.discovery)
                .addTab(HomeFragment(), R.string.mine).commit()

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE), 12345)
        }
    }

    override fun onViewPagerSelected(arg0: Int) {
        super.onViewPagerSelected(arg0)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //确保要请求的权限已在manifest声明，否则返回数组中该权限将为PackageManager.PERMISSION_DENIED
        if (requestCode == 12345 && !(grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
            AlertDialog.Builder(this).setCancelable(false).setMessage("很抱歉，APP正常运行需要您授予相关权限")
                    .setPositiveButton("关闭") { _, _ -> finish() }
                    .create().show()
        }
    }
}
