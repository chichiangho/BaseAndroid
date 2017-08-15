package com.chichiangho.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.chichiangho.base.R
import com.chichiangho.base.base.BaseFragment

/**
 * Created by chichiangho on 2017/6/6.
 */

class HomeFragment : BaseFragment() {
    private var rootView: View? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater!!.inflate(R.layout.fragment_home, null, false)
        return rootView
    }
}
