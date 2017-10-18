package com.chichiangho.base.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chichiangho.base.R
import com.chichiangho.base.base.BaseFragment
import com.chichiangho.base.extentions.*
import com.chichiangho.base.http.OkHttpClient
import com.chichiangho.base.http.RxHttpObserver
import com.chichiangho.base.pay.AliPrePayInfo
import com.chichiangho.base.pay.PayUtil
import com.chichiangho.base.pay.PrePayInfo
import com.chichiangho.base.widgets.DateTimePicker
import com.chichiangho.base.widgets.ImageSelectorView
import com.tencent.mm.opensdk.modelpay.PayReq
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import kotlinx.android.synthetic.main.fragment_make_order.*
import org.json.JSONObject
import java.util.*


/**
 * Created by chichiangho on 2017/7/13.
 */

class MakeOrderFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater?.inflate(R.layout.fragment_make_order, null, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        photo_selector.setMaxCount(7)
        photo_selector.setOnItemClickListener(object : ImageSelectorView.OnItemClickListener {
            override fun onItemClick(v: View, position: Int) {
                val intent = Intent()
                intent.type = "image/*"
                intent.action = Intent.ACTION_GET_CONTENT
                startActivityForResult(intent, SELECTOR_BASE + position)
            }
        })
        et_choose_time.setOnClickListener {
            DateTimePicker(context)
                    .setShowDate(Date(System.currentTimeMillis()))
                    .setFromThen(true)
                    .setOnDateTimePickedCallback(object : DateTimePicker.OnDateTimePickedCallback {
                        override fun onPicked(date: Date) {
                            et_choose_time.setText(date.time.formatDate("yyyy-MM-dd HH:mm"))
                        }
                    })
                    .show()
        }
        perform.text = 103000314233L.formatDHM()
        perform.setOnClickListener {
            Observable
                    .create(ObservableOnSubscribe<String> { e ->
                        val res = OkHttpClient["http://wxpay.wxutil.com/pub_v2/app/app_pay.php"].body().string()
                        e.onNextComplete(res)
                    })
                    .io_main()
                    .subscribe(object : RxHttpObserver<String>() {
                        override fun onSuccess(response: String) {
                            val json = JSONObject(response)
                            val aliInfo = AliPrePayInfo()
                            val weChatInfo = PayReq()
                            val pre = PrePayInfo()
//                        pre.ali = aliInfo
                            pre.weChat = weChatInfo

                            aliInfo.app_id = "00"
                            aliInfo.body = "yiersan"
                            aliInfo.appenv = "xxx"
                            aliInfo.goods_type = "xxx"
                            aliInfo.notify_url = "xxx"
                            aliInfo.out_trade_no = "xxx"

                            weChatInfo.appId = json.getString("appid")
                            PayUtil.wechat_app_id = weChatInfo.appId
                            weChatInfo.partnerId = json.getString("partnerid")
                            weChatInfo.prepayId = json.getString("prepayid")
                            weChatInfo.nonceStr = json.getString("noncestr")
                            weChatInfo.timeStamp = json.getString("timestamp")
                            weChatInfo.packageValue = json.getString("package")
                            weChatInfo.sign = json.getString("sign")
                            weChatInfo.extData = "app data"// optional
                            PayUtil(activity)
                                    .setPrePayInfo(pre)
                                    .pay(object : PayUtil.PayListener {
                                        override fun onSuccess() {
                                            toastShort("success")
                                        }

                                        override fun onFailed(memo: String) {
                                            toastShort(memo)
                                        }
                                    })
                        }
                    })
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null && data.data != null && requestCode >= SELECTOR_BASE) {
            val index = requestCode - SELECTOR_BASE
            photo_selector.setData(index, data.data)
        }
    }

    companion object {
        private val SELECTOR_BASE = 10000
    }
}
