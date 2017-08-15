package com.chichiangho.base.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chichiangho.base.R
import com.chichiangho.base.base.BaseFragment
import com.chichiangho.base.utils.TimeUtil
import com.chichiangho.base.widgets.DateTimePicker
import com.chichiangho.base.widgets.ImageSelectorView
import kotlinx.android.synthetic.main.fragment_make_order.*
import java.util.*

/**
 * Created by chichiangho on 2017/7/13.
 */

class MakeOrderFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_make_order, null, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        photo_selector.setMaxCount(7)
        photo_selector.setOnItemClickListener(object : ImageSelectorView.OnItemClickListener {
            override fun onItemClick(v: View, position: Int) {
                val intent = Intent()
                intent.type = "image/*"
                intent.action = Intent.ACTION_GET_CONTENT
                startActivityForResult(intent, SELECTOR_BASE + position)
            }
        })

        et_choose_time.setOnClickListener {
            DateTimePicker(context)
                    .setShowDate(Date(System.currentTimeMillis()))
                    .setFromThen(true)
                    .setOnDateTimePickedCallback(object : DateTimePicker.OnDateTimePickedCallback {
                        override fun onPicked(date: Date) {
                            et_choose_time.setText(TimeUtil.formatTimeAccurateMin(date.time))
                        }
                    })
                    .show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null && data.data != null && requestCode >= SELECTOR_BASE) {
            val index = requestCode - SELECTOR_BASE
            photo_selector.setData(index, data.data)
        }
    }

    companion object {
        private val SELECTOR_BASE = 10000
    }
}
