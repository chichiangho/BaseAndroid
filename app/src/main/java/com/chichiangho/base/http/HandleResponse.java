package com.chichiangho.base.http;

import com.chichiangho.base.base.BaseResponse;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * Created by chichiangho on 2017/4/28.
 */

public class HandleResponse<T> implements Function<T, Observable<T>> {

    @Override
    public Observable<T> apply(final T t) throws Exception {
        return Observable.create(e -> {
            if (t instanceof BaseResponse) {//错误逻辑处理在RxObserver
                BaseResponse response = (BaseResponse) t;
                switch (response.getCode()) {
                    case BaseResponse.CODE_SUCCESS:
                        e.onNext(t);
                        e.onComplete();
                        break;
                    case BaseResponse.CODE_TOKEN_TIMEOUT:
                        e.onError(new Throwable(response.getCode()));//因为要调用UI线程，交由onError处理
                        break;
                    default:
                        e.onError(new Throwable(response.getMsg()));
                        break;
                }
            }
        });
    }
}
