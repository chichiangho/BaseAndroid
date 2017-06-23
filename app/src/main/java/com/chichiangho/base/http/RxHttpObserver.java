package com.chichiangho.base.http;

import android.content.Intent;

import com.chichiangho.base.activity.LoginActivity;
import com.chichiangho.base.base.BaseApplication;
import com.chichiangho.base.base.BaseResponse;
import com.chichiangho.base.utils.ToastUtil;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by chichiangho on 2017/4/27.
 */

public abstract class RxHttpObserver<T> implements Observer<T> {

    @Override
    final public void onSubscribe(Disposable d) {
        onStart();
    }

    @Override
    final public void onNext(T value) {
        onSuccess(value);//错误码解析在HandleResponse
    }

    @Override
    final public void onError(Throwable e) {
        if (e != null && e.getMessage() != null) {
            switch (e.getMessage()) {
                case BaseResponse.CODE_TOKEN_TIMEOUT:
                    ToastUtil.showShort("token过期请重新登录");
                    Intent intent = new Intent(BaseApplication.getInstance(), LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    BaseApplication.getInstance().startActivity(intent);
                    break;
                default:
                    ToastUtil.showShort(e.getMessage().equals("") ? "网络错误，请稍后再试..." : e.getMessage());
                    break;
            }
        }

        onFailed();
        onComplete();
    }

    protected void onStart() {
    }

    protected abstract void onSuccess(T value);

    protected void onFailed() {
    }

    @Override
    public void onComplete() {
    }
}
