package com.chichiangho.base.widgets

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView

import com.chichiangho.base.R

class RefreshRecyclerView : SwipeRefreshLayout {
    private var listener: RefreshListener? = null
    private var recyclerView: RecyclerView? = null
    private var lastRequestTime: Long = 0
    private var touchEnd: Boolean = false//是否有更多数据，true没有
    private var notFill: Boolean = false//数据是否充满RecyclerView，true没有
    private var loadingMore: Boolean = false
    private var pulling: Boolean = false//是否在上拉手势中，true在
    private var footerView: RelativeLayout? = null
    private var footTv: TextView? = null
    private var footProgress: ProgressBar? = null
    private var valueAnimator: ValueAnimator? = null


    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    override fun onFinishInflate() {
        super.onFinishInflate()
        init()
    }

    internal fun init() {
        setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light)

        for (i in 0..childCount - 1) {
            if (getChildAt(i) is RecyclerView) {
                recyclerView = getChildAt(i) as RecyclerView
                break
            }
        }
        recyclerView ?: throw Exception("this view should got a recyclerView as child")

        recyclerView?.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {
                super.getItemOffsets(outRect, view, parent, state)
                outRect.bottom = 1
            }
        })//由于某些原因，无ItemDecoration时canScrollVertically()函数无法正确判断，故添加一个1px的ItemDecoration以修复
        recyclerView?.setOnTouchListener(object : View.OnTouchListener {
            var distance: Float = 0f
            var mFirstY: Float = 0f
            var motionStart: Float = 0f

            override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
                initFooter()
                if (touchEnd || notFill || loadingMore || isRefreshing)
                    return false

                if (valueAnimator?.isRunning ?: false)
                    return false

                when (motionEvent.action) {
                    MotionEvent.ACTION_MOVE -> {
                        if (motionStart == 0f)
                            motionStart = motionEvent.rawY
                        if (!pulling && !(recyclerView?.canScrollVertically(1) ?: false)) {
                            mFirstY = motionEvent.rawY
                            pulling = true
                        }
                        if (pulling) {
                            distance = (mFirstY - motionEvent.rawY) * 6 / 10//0.6的ratio
                            val params = footerView?.layoutParams
                            params?.height = distance.toInt()
                            footerView?.layoutParams = params
                        }
                    }
                    MotionEvent.ACTION_UP -> {
                        pulling = false
                        val motionEnd = motionEvent.rawY
                        if (distance > 0 && motionStart + 50 > motionEnd) {
                            motionStart = 0f
                            if (footerView != null) {
                                valueAnimator = ObjectAnimator.ofFloat(distance, resources.getDimension(R.dimen.refresh_footer_height)).setDuration(distance.toLong())
                                valueAnimator?.addUpdateListener { animation ->
                                    val nowDistance = animation.animatedValue as Float
                                    val layoutParams = footerView?.layoutParams
                                    layoutParams?.height = nowDistance.toInt()
                                    footerView?.layoutParams = layoutParams
                                }
                                valueAnimator?.addListener(object : AnimatorListenerAdapter() {
                                    override fun onAnimationEnd(animation: Animator) {
                                        super.onAnimationEnd(animation)
                                        footTv?.text = "加载中..."
                                        footProgress?.visibility = View.VISIBLE

                                        val cur = System.currentTimeMillis()
                                        if (listener != null && cur - lastRequestTime > REFRESH_INTERVAL) {
                                            lastRequestTime = cur
                                            loadingMore = true
                                            listener?.onLoadMore()
                                        }
                                    }
                                })
                                valueAnimator?.start()
                            } else {
                                val cur = System.currentTimeMillis()
                                if (listener != null && cur - lastRequestTime > REFRESH_INTERVAL) {
                                    lastRequestTime = cur
                                    loadingMore = true
                                    listener?.onLoadMore()
                                }
                            }
                        }
                    }
                }
                return false
            }
        })

        setOnRefreshListener {
            val cur = System.currentTimeMillis()
            if (listener != null && !pulling && !loadingMore && cur - lastRequestTime > REFRESH_INTERVAL) {
                if (footerView != null) {
                    val layoutParams = footerView?.layoutParams
                    layoutParams?.height = 0
                    footerView?.layoutParams = layoutParams
                }
                touchEnd = false
                lastRequestTime = cur
                listener?.onRefresh()
            } else {
                isRefreshing = false
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        notFill = !(recyclerView?.canScrollVertically(1) ?: false || recyclerView?.canScrollVertically(-1) ?: false)
    }

    private fun initFooter() {
        if (footerView == null && recyclerView?.adapter is RecyclerViewFooterAdapter<*>) {
            footerView = LayoutInflater.from(context).inflate(R.layout.layout_refresh_footer, null, false) as RelativeLayout
            footTv = footerView?.findViewById(R.id.footerTv) as TextView
            footProgress = footerView?.findViewById(R.id.footerProgress) as ProgressBar
            val params = RelativeLayout.LayoutParams(measuredWidth, 0)
            footerView?.layoutParams = params
            footTv?.text = "上拉加载更多"
            footProgress?.visibility = View.GONE
            (recyclerView?.adapter as RecyclerViewFooterAdapter<*>).footerView = footerView
        }
    }

    override fun setRefreshing(refreshing: Boolean) {
        if (footerView != null) {
            if (loadingMore) {
                val start: Float = footerView?.layoutParams?.height?.toFloat() as Float
                valueAnimator = ObjectAnimator.ofFloat(start, 0f).setDuration(500)
                valueAnimator?.addUpdateListener { animation ->
                    val nowDistance = animation.animatedValue as Float
                    val params = footerView?.layoutParams
                    params?.height = nowDistance.toInt()
                    footerView?.layoutParams = params
                }
                valueAnimator?.addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        super.onAnimationEnd(animation)
                        if (touchEnd) {
                            footerView?.layoutParams?.height = resources.getDimension(R.dimen.refresh_footer_height).toInt()
                            footTv?.text = "我是有底线的"
                            footProgress?.visibility = View.GONE
                        } else {
                            footTv?.text = "上拉加载更多"
                            footProgress?.visibility = View.GONE
                        }
                        loadingMore = false
                    }
                })
                valueAnimator?.start()
            } else {
                footTv?.text = "上拉加载更多"
                footProgress?.visibility = View.GONE
            }
        } else {
            loadingMore = false
        }

        super.setRefreshing(refreshing)
    }

    fun setNoMoreData() {
        touchEnd = true
        if (!(valueAnimator?.isRunning ?: true)) {
            footerView?.layoutParams?.height = resources.getDimension(R.dimen.refresh_footer_height).toInt()
            footTv?.text = "我是有底线的"
            footProgress?.visibility = View.GONE
        }
    }

    fun setListener(listener: RefreshListener) {
        this.listener = listener
    }

    interface RefreshListener {
        fun onRefresh()

        fun onLoadMore()
    }

    companion object {
        private val TAG = "RefreshRecyclerView"
        private val REFRESH_INTERVAL = 1000
    }
}