package com.chichiangho.base.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import com.chichiangho.base.R
import com.chichiangho.base.base.BaseTitleActivity
import com.chichiangho.base.fragment.MakeOrderFragment
import com.chichiangho.base.fragment.TakeOrderFragment
import kotlinx.android.synthetic.main.activity_dailian.*


/**
 * Created by chichiangho on 2017/3/23.
 */

class DailianActivity : BaseTitleActivity() {

    private lateinit var takeOrderFragment: TakeOrderFragment
    private lateinit var makeOrderFragment: MakeOrderFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dailian)
        setTitle("游戏代练")

        takeOrderFragment = TakeOrderFragment()
        makeOrderFragment = MakeOrderFragment()

        supportFragmentManager.beginTransaction().add(R.id.container, takeOrderFragment).add(R.id.container, makeOrderFragment).hide( makeOrderFragment).commit()

        radioLeft.setOnClickListener {
            supportFragmentManager.beginTransaction().show(takeOrderFragment).hide(makeOrderFragment).commit()
        }
        radioRight.setOnClickListener {
            supportFragmentManager.beginTransaction().show(makeOrderFragment).hide(takeOrderFragment).commit()
        }
        radioLeft.performClick()
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE), 12345)
        }
    }
}
