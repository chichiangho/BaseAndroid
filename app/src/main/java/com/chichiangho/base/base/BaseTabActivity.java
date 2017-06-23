package com.chichiangho.base.base;

import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chichiangho.base.R;
import com.chichiangho.base.utils.DeviceUtil;
import com.chichiangho.base.widgets.TabViewPager;

import java.util.ArrayList;

public class BaseTabActivity extends BaseTitleActivity {

    private MyAdapter mAdapter;
    private TabViewPager mViewPager;
    private LinearLayout mTab;

    private ArrayList<Holder> mList = new ArrayList<>(4);
    private int mTabBgColor = R.color.white;
    private int mTabHeight = R.dimen.tab_height;
    private int mTextSize = R.dimen.size_middle;
    private int mTextNormal = R.color.tab_text_normal;
    private int mTextChecked = R.color.tab_text_checked;
    private Holder cur;
    private boolean needDivider;
    private boolean mNeedUnderHint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);

        mViewPager = (TabViewPager) findViewById(R.id.view_pager);
        mAdapter = new MyAdapter(getSupportFragmentManager());
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                onViewPagerSelected(arg0);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

    protected void onViewPagerSelected(int arg0) {
        if (cur != null) {
            cur.getTv().setTextColor(getResources().getColor(mTextNormal));
            cur.getIv().setImageResource(cur.getIn());
            if (mNeedUnderHint)
                cur.getUnder().setVisibility(View.GONE);
        }
        cur = mList.get(arg0);
        cur.getTv().setTextColor(getResources().getColor(mTextChecked));
        cur.getIv().setImageResource(cur.getIs());
        if (mNeedUnderHint)
            cur.getUnder().setVisibility(View.VISIBLE);
    }

    public BaseTabActivity prepare(@DimenRes int tabHeight, @DimenRes int textSize, @ColorRes int textNormalColor, @ColorRes int textCheckedColor, @ColorRes int bgColor, boolean needUnderHint) {
        mTabHeight = tabHeight;
        mTextSize = textSize;
        mTextNormal = textNormalColor;
        mTextChecked = textCheckedColor;
        mTabBgColor = bgColor;
        mNeedUnderHint = needUnderHint;
        return this;
    }

    public BaseTabActivity addTab(Fragment fragment, int tabTextRes) {
        return addTab(fragment, tabTextRes, 0, 0);
    }

    public BaseTabActivity addTab(Fragment fragment, int tabTextRes, int imgNormal, int imgSelected) {
        Holder holder = new Holder(LayoutInflater.from(this).inflate(R.layout.layout_tab_item, null), fragment, imgNormal, imgSelected);
        mList.add(holder);
        holder.getTv().setText(tabTextRes);
        holder.getV().setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < mList.size(); i++) {
                    if (v == mList.get(i).getV()) {
                        mViewPager.setCurrentItem(i);
                    }
                }
            }
        });
        return this;
    }

    public BaseTabActivity setNeedDivider(boolean needDivider) {
        this.needDivider = needDivider;
        return this;
    }

    public void commit() {
        if (mList.get(0).getIv().getVisibility() == View.GONE) {//if no img,which means tab at top
            mTab = (LinearLayout) findViewById(R.id.top_layout);
            findViewById(R.id.top_divider).setVisibility(View.VISIBLE);
        } else {
            mTab = (LinearLayout) findViewById(R.id.bottom_layout);
            findViewById(R.id.bottom_divider).setVisibility(View.VISIBLE);
        }
        mTab.setBackgroundColor(getResources().getColor(mTabBgColor));
        if (mList.size() > 1)
            mTab.setVisibility(View.VISIBLE);

        for (int i = 0; i < mList.size(); i++) {
            View tabItem = mList.get(i).getV();

            RelativeLayout.LayoutParams radioParams = new RelativeLayout.LayoutParams(DeviceUtil.getScreenWidth()
                    / mList.size(), (int) getResources().getDimension(mTabHeight));
            tabItem.setLayoutParams(radioParams);

            mTab.addView(tabItem);

            if (needDivider) {
                ImageView divider = new ImageView(getBaseContext());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) getResources().getDimension(R.dimen.divider_width), (int) (getResources().getDimension(mTabHeight) * 2 / 3));
                params.gravity = Gravity.CENTER;
                divider.setLayoutParams(params);
                divider.setBackgroundResource(R.color.divider_tab);
                mTab.addView(divider);
            }
            if (mNeedUnderHint) {
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                        (DeviceUtil.getScreenWidth() / mList.size()) / 2, (int) getResources().getDimension(R.dimen.tab_divider_height));
                params.addRule(RelativeLayout.CENTER_HORIZONTAL);
                params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                mList.get(i).getUnder().setLayoutParams(params);
                mList.get(i).getUnder().setBackgroundResource(mTextChecked);
            } else {
                mList.get(i).getUnder().setVisibility(View.GONE);
            }
        }

        mAdapter.notifyDataSetChanged();
        onViewPagerSelected(0);
    }

    public TabViewPager getViewPager() {
        return mViewPager;
    }

    private class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int arg0) {
            return mList.get(arg0).getF();
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
        }
    }

    private class Holder {
        private Fragment f;
        private View v;
        private TextView tv;
        private ImageView iv;
        private View under;
        private int in;
        private int is;

        Holder(View v, Fragment f, int in, int is) {
            this.f = f;
            this.v = v;
            tv = (TextView) v.findViewById(R.id.text);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(mTextSize));
            iv = (ImageView) v.findViewById(R.id.image);
            under = v.findViewById(R.id.under_hint);
            this.in = in;
            this.is = is;

            if (in == 0 || is == 0)
                iv.setVisibility(View.GONE);
            else if (in != 0)
                iv.setImageResource(in);
        }


        public TextView getTv() {
            return tv;
        }

        public ImageView getIv() {
            return iv;
        }

        public int getIn() {
            return in;
        }

        public int getIs() {
            return is;
        }

        public Fragment getF() {
            return f;
        }

        public View getV() {
            return v;
        }

        public View getUnder() {
            return under;
        }

        public void setUnder(View under) {
            this.under = under;
        }
    }
}
