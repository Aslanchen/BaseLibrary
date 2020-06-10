package com.aslan.baselibrary.http;

import android.content.Context;

import com.aslan.baselibrary.R;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 网络回调基类，主要负责异常封装，以及数据序列化。
 *
 * @author Aslan
 * @date 2019/9/23
 */
public abstract class BaseObserver<T> implements Observer<T> {

    private Context context;

    public BaseObserver(Context context) {
        this.context = context;
    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof BaseError) {
            handleError((BaseError) e);
        } else {
            handleError(new BaseError(NetManager.ERROR_OTHER, e.getMessage()));
        }
    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onSubscribe(Disposable d) {
        if (!NetManager.isNetworkAvailable(context)) {
            handleError(new BaseError(NetManager.ERROR_NO_NET,
                    context.getString(R.string.error_net_no_net)));
        }
    }

    @Override
    public void onNext(T t) {
        handleSuccess(t);
    }

    public abstract void handleError(BaseError e);

    public abstract void handleSuccess(T t);
}