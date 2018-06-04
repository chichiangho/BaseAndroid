package com.chichiangho.base.fragment

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.chichiangho.base.R
import com.chichiangho.base.activity.DailianDetailActivity
import com.chichiangho.base.bean.Data
import com.chichiangho.common.base.BaseFragment
import com.chichiangho.common.extentions.hideLoading
import com.chichiangho.common.extentions.logD
import com.chichiangho.common.extentions.showLoading
import com.chichiangho.widget.recyclerview.extension.RecyclerViewFooterAdapter
import com.chichiangho.widget.recyclerview.extension.RefreshRecyclerView
import kotlinx.android.synthetic.main.fragment_take_order.*
import kotlinx.android.synthetic.main.item_dailian_trade.view.*
import java.util.*

/**
 * Created by chichiangho on 2017/7/13.
 */

class TakeOrderFragment : BaseFragment() {
    private val mainList = ArrayList<Data>(10)
    private val types = arrayOf("全部类型", "普通代练", "优质代练", "押金代练")
    private val states = arrayOf("全部交易", "未交易", "已交易")
    private val sorts = arrayOf("价格排序", "从低到高", "从高到低")
    private lateinit var adapter: Adapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_take_order, null, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        logD("xx","yy")
        recycler.layoutManager = LinearLayoutManager(context)
        adapter = Adapter()
        recycler.adapter = adapter
        recycler.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {
                outRect.bottom = resources.getDimension(R.dimen.main_margin).toInt()
            }
        })
        refresh.setListener(object : RefreshRecyclerView.RefreshListener {
            override fun onRefresh() {
                showLoading("loading")
                Handler().postDelayed({
                    refresh.isRefreshing = false
                    adapter.setData(mainList.clone() as ArrayList<Data>)
                    hideLoading()
                }, 2000)
            }

            override fun onLoadMore() {
//                showLoading("loading")
                Handler().postDelayed({
                    refresh.isRefreshing = false
                    adapter.appendData(mainList.clone() as ArrayList<Data>)
                    hideLoading()
                }, 2000)
            }
        })

        btn_choose_game.setOnClickListener { startActivityForResult(Intent(activity, DailianDetailActivity::class.java), 10086) }
        btn_choose_type.setOnClickListener { showPopup(btn_choose_type_upper, types, AdapterView.OnItemClickListener { _, _, i, _ -> btn_choose_type.text = types[i] }) }
        btn_choose_state.setOnClickListener { showPopup(btn_choose_state_upper, states, AdapterView.OnItemClickListener { _, _, i, _ -> btn_choose_state.text = states[i] }) }
        btn_choose_sort.setOnClickListener { showPopup(btn_choose_sort_upper, sorts, AdapterView.OnItemClickListener { _, _, i, _ -> btn_choose_sort.text = sorts[i] }) }

        adapter.setData(mainList.clone() as ArrayList<Data>)
        mainList.add(Data())
        mainList.add(Data())
        mainList.add(Data())
        mainList.add(Data())
//        adapter.setData(mainList)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 10086) {

        }
    }

    private fun showPopup(anchorView: View, datas: Array<String>, listener: AdapterView.OnItemClickListener) {
        val mPopup = ListPopupWindow(context)
        val adapter = ArrayAdapter(context, R.layout.item_textview, datas)
        mPopup.setAdapter(adapter)
        mPopup.width = AbsListView.LayoutParams.WRAP_CONTENT
        mPopup.height = AbsListView.LayoutParams.WRAP_CONTENT
        mPopup.isModal = true
        mPopup.setOnItemClickListener { adapterView, view, i, l ->
            mPopup.dismiss()
            listener.onItemClick(adapterView, view, i, l)
        }
        mPopup.anchorView = anchorView
        mPopup.show()
    }

    private inner class Adapter : RecyclerViewFooterAdapter<Data>() {

//        override fun getEmptyView(): View? {
//            return layoutInflater.inflate(R.layout.layout_title,null)
//        }

        override fun onCreate(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return Holder(LayoutInflater.from(context).inflate(R.layout.item_dailian_trade, null, false))
        }

        override fun onBind(holder: RecyclerView.ViewHolder, position: Int, data: Data) {
            (holder as Holder).image.setImageResource(R.drawable.ic_launcher)
            holder.detail.text = "xxxxxxxxxxxxxxxxxxxx"
            holder.type.text = "押金代练"
            holder.price.text = "￥30.00"
            holder.platform.text = "IOS"
            holder.validTime.text = "要求完成时间：" + "10:00-11:00"
            holder.root.setOnClickListener {
                val intent = Intent(activity, DailianDetailActivity::class.java)
                intent.putExtra("data", data)
                startActivity(intent)
            }
        }

        private inner class Holder(val root: View) : RecyclerView.ViewHolder(root) {
            val image: ImageView = root.image
            val detail: TextView = root.detail
            val type: TextView = root.type
            val price: TextView = root.price
            val platform: TextView = root.platform
            val validTime: TextView = root.valid_time
        }
    }
}
