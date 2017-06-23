package com.chichiangho.base.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chichiangho.base.R;


public class TitleLayout extends LinearLayout {

    public TitleLayout(Context context) {
        this(context, null);
    }

    public TitleLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(attrs);
    }

    /**
     * 初始化属性
     *
     * @param attrs
     */
    public void initView(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.title_bar);
        int leftImgRes = typedArray.getResourceId(R.styleable.title_bar_left_img, 0);
        String leftTvText = typedArray.getString(R.styleable.title_bar_left_text);
        int rightImgRes = typedArray.getResourceId(R.styleable.title_bar_right_img, 0);
        String rightTvText = typedArray.getString(R.styleable.title_bar_right_text);
        String titleText = typedArray.getString(R.styleable.title_bar_title);
        int bgRes = typedArray.getResourceId(R.styleable.title_bar_titleBackground, 0);
        typedArray.recycle();

        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.layout_title, null, false);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen.title_height));
        rootView.setLayoutParams(params);

        if (leftImgRes != 0) {
            ImageView leftImg = (ImageView) rootView.findViewById(R.id.left_img);
            leftImg.setVisibility(VISIBLE);
            leftImg.setBackgroundResource(leftImgRes);
        }
        if (leftTvText != null) {
            TextView leftTv = (TextView) rootView.findViewById(R.id.left_tv);
            leftTv.setVisibility(VISIBLE);
            leftTv.setText(leftTvText);
        }
        if (rightImgRes != 0) {
            ImageView rightImg = (ImageView) rootView.findViewById(R.id.right_img);
            rightImg.setVisibility(VISIBLE);
            rightImg.setBackgroundResource(rightImgRes);
        }
        if (rightTvText != null) {
            TextView rightTv = (TextView) rootView.findViewById(R.id.right_tv);
            rightTv.setVisibility(VISIBLE);
            rightTv.setText(rightTvText);
        }
        if (titleText != null) {
            TextView titleTv = (TextView) rootView.findViewById(R.id.title_tv);
            titleTv.setText(titleText);
        }

        if (bgRes != 0) {
            RelativeLayout barRlyt = (RelativeLayout) rootView.findViewById(R.id.content_layout);
            barRlyt.setBackgroundColor(bgRes);
        }

        addView(rootView, 0);
    }
}