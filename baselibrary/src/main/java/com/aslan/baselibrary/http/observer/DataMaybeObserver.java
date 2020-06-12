package com.aslan.baselibrary.http.observer;

import android.content.Context;

import com.aslan.baselibrary.http.BaseError;
import com.aslan.baselibrary.http.NetManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.reactivex.MaybeObserver;
import io.reactivex.disposables.Disposable;

/**
 * 网络回调基类，主要负责异常封装，以及数据序列化。
 *
 * @author Aslan
 * @date 2019/9/23
 */
public abstract class DataMaybeObserver<T> implements MaybeObserver<T> {

    private Context context;

    public DataMaybeObserver(Context context) {
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
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onComplete() {
        handleSuccess(null);
    }

    @Override
    public void onSuccess(@NonNull T t) {
        handleSuccess(t);
    }

    public abstract void handleError(@NonNull BaseError e);

    public abstract void handleSuccess(@Nullable T t);
}