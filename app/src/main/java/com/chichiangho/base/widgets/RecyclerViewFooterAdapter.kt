package com.chichiangho.base.widgets

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import android.view.ViewGroup
import java.util.*

abstract class RecyclerViewFooterAdapter<T> : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var mDatas = ArrayList<T>()
    var footerView: View? = null
        set(footerView) {
            field = footerView
            notifyItemInserted(mDatas.size + 1)
        }
    private var mListener: OnItemClickListener<T>? = null

    fun setOnItemClickListener(li: OnItemClickListener<T>) {
        mListener = li
    }

    fun setData(datas: ArrayList<T>) {
        mDatas = datas
        notifyDataSetChanged()
    }

    fun appendData(datas: ArrayList<T>) {
        mDatas.addAll(datas)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        if (footerView == null)
            return TYPE_NORMAL
        if (position == mDatas.size)
            return TYPE_FOOTER
        return TYPE_NORMAL
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == TYPE_FOOTER)
            footerView?.let {
                return object : RecyclerView.ViewHolder(footerView) {}
            }
        return onCreate(parent, viewType)
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == TYPE_FOOTER) return
        val data = mDatas[position]
        onBind(viewHolder, position, data)
        viewHolder.itemView.setOnClickListener { v -> mListener?.onItemClick(v, data) }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView?) {
        super.onAttachedToRecyclerView(recyclerView)
        val manager = recyclerView?.layoutManager
        if (manager is GridLayoutManager) {
            manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (getItemViewType(position) == TYPE_FOOTER)
                        manager.spanCount
                    else
                        1
                }
            }
        }
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder?) {
        super.onViewAttachedToWindow(holder)
        val lp = holder?.itemView?.layoutParams
        if (lp != null && lp is StaggeredGridLayoutManager.LayoutParams
                && holder.layoutPosition == 0) {
            lp.isFullSpan = true
        }
    }

    override fun getItemCount(): Int {
        return if (footerView == null) mDatas.size else mDatas.size + 1
    }

    abstract fun onCreate(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder

    /**
     * 由于会使用数据数组进行一些计算，请务必使用[&lt;][.setData]来设置数据源，然后通过参数data来获取数据

     * @param holder
     * *
     * @param position
     * *
     * @param data
     */
    abstract fun onBind(holder: RecyclerView.ViewHolder, position: Int, data: T)

    interface OnItemClickListener<in T> {
        fun onItemClick(v: View, data: T)
    }

    companion object {
        val TYPE_FOOTER = 0
        val TYPE_NORMAL = 1
    }
}