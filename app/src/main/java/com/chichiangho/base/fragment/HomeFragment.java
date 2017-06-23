package com.chichiangho.base.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chichiangho.base.R;
import com.chichiangho.base.base.BaseFragment;

/**
 * Created by chichiangho on 2017/6/6.
 */

public class HomeFragment extends BaseFragment {
    private View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home,null,false);
        return rootView;
    }
}
