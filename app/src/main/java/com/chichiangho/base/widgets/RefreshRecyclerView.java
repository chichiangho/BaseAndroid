package com.chichiangho.base.widgets;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chichiangho.base.utils.DeviceUtil;
import com.chichiangho.base.utils.Logger;
import com.chichiangho.base.R;

public class RefreshRecyclerView extends SwipeRefreshLayout {
    private static final String TAG = "RefreshRecyclerView";
    private static final int REFRESH_INTERVAL = 1000;
    private RefreshListener listener;
    private RecyclerView recyclerView;
    private RelativeLayout footerView;
    private long lastRequestTime;
    private boolean touchEnd;
    private boolean loadingMore;
    private Type managerType;
    private RecyclerView.LayoutManager manager;
    private TextView footTv;
    private ProgressBar footProgress;
    private int hideCount = 1;

    private enum Type {Linear, Grid, StaggeredGrid}

    public RefreshRecyclerView(Context context) {
        super(context);
    }

    public RefreshRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        init();
    }

    public void setHideFooterWhenItemLessThan(int itemCount) {
        hideCount = itemCount;
    }

    void init() {
        setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);

        recyclerView = (RecyclerView) getChildAt(1);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private int lastVisibleItem;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (listener != null && !touchEnd && !isRefreshing() && !loadingMore
                        && recyclerView.getAdapter().getItemCount() > (recyclerView.getAdapter() instanceof RecyclerViewFooterAdapter ? 1 : 0)
                        && newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == recyclerView.getAdapter().getItemCount()) {
                    long cur = System.currentTimeMillis();
                    if (cur - lastRequestTime > REFRESH_INTERVAL) {
                        if (recyclerView.getAdapter() instanceof RecyclerViewFooterAdapter) {
                            initFooter();
                            if (recyclerView.getAdapter().getItemCount() > hideCount) {
                                footerView.setVisibility(VISIBLE);
                                footTv.setText("loading...");
                                footProgress.setVisibility(VISIBLE);
                            } else {
                                footerView.setVisibility(GONE);
                            }
                        }
                        lastRequestTime = cur;
                        loadingMore = true;
                        listener.onLoadMore();
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (manager == null) {
                    manager = recyclerView.getLayoutManager();
                    if (manager instanceof LinearLayoutManager)
                        managerType = Type.Linear;
                    else if (manager instanceof GridLayoutManager)
                        managerType = Type.Grid;
                    else if (manager instanceof StaggeredGridLayoutManager)
                        managerType = Type.StaggeredGrid;
                }
                switch (managerType) {
                    case Linear:
                        lastVisibleItem = ((LinearLayoutManager) manager).findLastVisibleItemPosition();
                        break;
                    case Grid:
                        lastVisibleItem = ((GridLayoutManager) manager).findLastVisibleItemPosition();
                        break;
                    case StaggeredGrid:
                        int[] positions = new int[((StaggeredGridLayoutManager) manager).getSpanCount()];
                        ((StaggeredGridLayoutManager) manager).findLastVisibleItemPositions(positions);
                        int max = 0;
                        for (int value : positions) {
                            if (value > max) {
                                max = value;
                            }
                        }
                        lastVisibleItem = max;
                        break;
                    default:
                        Logger.e(TAG, "LayoutManager must be LinearLayoutManager or GridLayoutManager or StaggeredGridLayoutManager");
                        break;
                }
            }
        });

        setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                long cur = System.currentTimeMillis();
                if (listener != null && !loadingMore && cur - lastRequestTime > REFRESH_INTERVAL) {
                    touchEnd = false;
                    listener.onRefresh();
                } else {
                    setRefreshing(false);
                }
            }
        });
    }

    private void initFooter() {
        if (footerView == null) {
            footerView = (RelativeLayout) LayoutInflater.from(getContext()).inflate(R.layout.layout_refresh_footer, null, false);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(DeviceUtil.getScreenWidth(), (int) getResources().getDimension(R.dimen.refresh_footer_height));
            footerView.setLayoutParams(params);
            footTv = (TextView) footerView.findViewById(R.id.footer_tv);
            footProgress = (ProgressBar) footerView.findViewById(R.id.footer_progress);

            ((RecyclerViewFooterAdapter) recyclerView.getAdapter()).setFooterView(footerView);
        }
    }

    @Override
    public void setRefreshing(final boolean refreshing) {
        loadingMore = false;
        if (recyclerView.getAdapter() instanceof RecyclerViewFooterAdapter) {
            initFooter();
            if (recyclerView.getAdapter().getItemCount() > hideCount) {
                footerView.setVisibility(VISIBLE);
                if (!touchEnd)
                    footTv.setText("上拉加载更多");
                footProgress.setVisibility(GONE);
            } else {
                footerView.setVisibility(GONE);
            }
        }
        super.setRefreshing(refreshing);
    }

    public void setNoMoreData() {
        touchEnd = true;
        if (footerView != null) {
            if (recyclerView.getAdapter().getItemCount() > hideCount) {
                footerView.setVisibility(VISIBLE);
                footTv.setText("我是有底线的");
                footProgress.setVisibility(GONE);
            } else {
                footerView.setVisibility(GONE);
            }
        }
    }

    public void setListener(RefreshListener listener) {
        this.listener = listener;
    }

    public interface RefreshListener {
        void onRefresh();

        void onLoadMore();
    }
}