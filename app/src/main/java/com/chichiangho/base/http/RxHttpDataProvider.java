package com.chichiangho.base.http;


import com.chichiangho.base.base.ProjectDefines;
import com.chichiangho.base.bean.User;
import com.chichiangho.base.utils.PreferenceUtil;
import com.chichiangho.base.utils.SignUtil;

import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * Created by chichiangho on 2017/3/17.
 */

public class RxHttpDataProvider {

    private static String getUrl(String method) {
        return ProjectDefines.HTTP_BASE + ProjectDefines.URL_BASE + method;
    }

    /**
     * 构建请求参数
     *
     * @param bizParams
     * @param subSignParams
     * @return
     */
    private static RequestParam getRequestParam(BizParam bizParams, HashMap<String, String> subSignParams) {
        RequestParam requestParam = new RequestParam();
        requestParam.setAppKey(ProjectDefines.APP_KEY);
        requestParam.setTimestamp(System.currentTimeMillis());


        HashMap<String, String> signParams = new HashMap<>();
        signParams.put("appKey", requestParam.getAppKey());
        signParams.put("timestamp", requestParam.getTimestamp() + "");

        User user = PreferenceUtil.getUser();
        if (user != null) {
            requestParam.setToken(user.getUserToken());
            signParams.put("token", requestParam.getToken());

            requestParam.setUserId(user.getUserId());
            signParams.put("userId", requestParam.getUserId() + "");
        }

        if (!subSignParams.isEmpty())
            signParams.putAll(subSignParams);
        requestParam.setSign(SignUtil.getSign(signParams, ProjectDefines.APP_SECRET));

        requestParam.setParams(bizParams);

        return requestParam;
    }

    /*******************interfaces begin*********************/

    /**
     * 接口示例
     *
     * @param cellPhone
     */
    public static <T> Observable<T> example(final String cellPhone, final Class<T> clz) {

        return Observable.create(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(ObservableEmitter<T> e) throws Exception {

                BizParam bizParams = new BizParam();
//                bizParams.setCellPhone(cellPhone);

                HashMap<String, String> signParams = new HashMap<>();
//                signParams.put("cellPhone", cellPhone);

                RequestParam requestParam = getRequestParam(bizParams, signParams);

                e.onNext(OkHttpClient.post(getUrl("notice/get_notice_info"), requestParam, clz));
                e.onComplete();//must call in every Observable,or onComplete() in Observer will not be called
            }
        }).flatMap(new HandleResponse<T>());
    }

}
