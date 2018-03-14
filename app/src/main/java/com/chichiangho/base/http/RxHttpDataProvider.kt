package com.chichiangho.base.http


import com.chichaingho.library_rx_okhttp.HandleResponse
import com.chichaingho.library_rx_okhttp.OkHttpClient
import com.chichiangho.base.base.AppConfigs
import com.chichiangho.base.utils.Preference
import com.chichiangho.base.utils.SignUtil
import com.chichiangho.common.extentions.onNextComplete
import com.chichiangho.common.extentions.toJson
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import java.util.*

/**
 * Created by chichiangho on 2017/3/17.
 */

object RxHttpDataProvider {

    private fun getUrl(method: String): String = AppConfigs.HTTP_BASE + AppConfigs.URL_BASE + method

    /**
     * 构建请求参数

     * @param bizParams
     * *
     * @param subSignParams
     * *
     * @return
     */
    private fun getRequestParam(bizParams: BizParam, subSignParams: HashMap<String, String>): RequestParam {
        val requestParam = RequestParam()
        requestParam.appKey = AppConfigs.APP_KEY
        requestParam.timestamp = System.currentTimeMillis()


        val signParams = HashMap<String, String>()
        signParams.put("appKey", requestParam.appKey as String)
        signParams.put("timestamp", requestParam.timestamp?.toString() ?: "")

        if (Preference.loginUser != null) {
            requestParam.token = Preference.loginUser?.userToken
            signParams.put("token", requestParam.token as String)

            requestParam.userId = Preference.loginUser?.userId
            signParams.put("userId", requestParam.userId?.toString() ?: "")
        }

        if (!subSignParams.isEmpty())
            signParams.putAll(subSignParams)
        requestParam.sign = SignUtil.getSign(signParams, AppConfigs.APP_SECRET)

        requestParam.params = bizParams

        return requestParam
    }

    /*******************interfaces begin */

    /**
     * 接口示例

     * @param cellPhone
     */
    fun <T> example(cellPhone: String, clz: Class<T>): Observable<T> {

        return Observable.create(ObservableOnSubscribe<T> { e ->
            val bizParams = BizParam()
            //                bizParams.setCellPhone(cellPhone);

            val signParams = HashMap<String, String>()
            //                signParams.put("cellPhone", cellPhone);

            val requestParam = getRequestParam(bizParams, signParams)

            e.onNextComplete(OkHttpClient.post(getUrl("notice/get_notice_info"), requestParam.toJson(), clz))
        }).map(HandleResponse<T>())
    }

}
