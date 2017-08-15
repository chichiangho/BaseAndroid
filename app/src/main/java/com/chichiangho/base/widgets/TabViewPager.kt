package com.chichiangho.base.widgets

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent

class TabViewPager : ViewPager {

    private var scrollAble = true

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context) : super(context)

    fun setScrollAble(scrollAble: Boolean) {
        this.scrollAble = scrollAble
    }

    override fun onInterceptTouchEvent(arg0: MotionEvent): Boolean {
        if (scrollAble) {
            return super.onInterceptTouchEvent(arg0)
        } else {
            return false
        }
    }

    override fun onTouchEvent(arg0: MotionEvent): Boolean {
        if (scrollAble) {
            return super.onTouchEvent(arg0)
        } else {
            return false
        }
    }
}
