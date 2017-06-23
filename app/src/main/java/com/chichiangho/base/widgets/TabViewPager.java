package com.chichiangho.base.widgets;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class TabViewPager extends ViewPager {

    private boolean scrollAble = true;

    public TabViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TabViewPager(Context context) {
        super(context);
    }

    public void setScrollAble(boolean scrollAble) {
        this.scrollAble = scrollAble;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        if (scrollAble) {
            return super.onInterceptTouchEvent(arg0);
        } else {
            return false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        if (scrollAble) {
            return super.onTouchEvent(arg0);
        } else {
            return false;
        }
    }
}
