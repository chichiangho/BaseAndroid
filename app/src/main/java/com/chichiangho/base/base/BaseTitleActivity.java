package com.chichiangho.base.base;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chichiangho.base.R;

public class BaseTitleActivity extends BaseActivity {
    private ImageView rightImg;
    private TextView rightTv;
    private TextView leftTv;
    private RelativeLayout title;
    private TextView titleTv;
    private ImageView leftImg;

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        title = (RelativeLayout) findViewById(R.id.content_layout);

        leftTv = (TextView) title.findViewById(R.id.left_tv);
        leftImg = (ImageView) title.findViewById(R.id.left_img);

        View.OnClickListener leftClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        };

        leftTv.setOnClickListener(leftClickListener);
        leftImg.setOnClickListener(leftClickListener);
        rightImg = (ImageView) title.findViewById(R.id.right_img);
        rightTv = (TextView) title.findViewById(R.id.right_tv);
        titleTv = (TextView) title.findViewById(R.id.title_tv);
    }

    protected ImageView getTitleLeftImg() {
        return leftImg;
    }

    protected TextView getTitleLeftTv() {
        return leftTv;
    }

    protected ImageView getTitleRightImg() {
        return rightImg;
    }

    protected TextView getTitleRightTv() {
        return rightTv;
    }

    protected void setTitleLeftClickListener(View.OnClickListener leftClickListener) {
        leftTv.setOnClickListener(leftClickListener);
        leftImg.setOnClickListener(leftClickListener);
    }

    protected void setTitleRightClickListener(View.OnClickListener rightClickListener) {
        rightImg.setOnClickListener(rightClickListener);
        rightTv.setOnClickListener(rightClickListener);
    }

    protected void hideTitle() {
        title.setVisibility(View.GONE);
    }

    protected void setTitleLeftTxt(String leftTxt) {
        leftTv.setVisibility(View.VISIBLE);
        leftTv.setText(leftTxt);
    }

    protected void setTitleRightTxt(String rightTxt) {
        rightTv.setVisibility(View.VISIBLE);
        rightTv.setText(rightTxt);
    }

    protected void setTitleRightImgRes(int rightImgRes) {
        rightImg.setVisibility(View.VISIBLE);
        rightImg.setImageResource(rightImgRes);
    }

    protected void setTitleBackground(int bgRes) {
        title.setBackgroundResource(bgRes);
    }

    protected void setTitle(String titleTxt) {
        titleTv.setText(titleTxt);
    }
}
