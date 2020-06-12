package com.aslan.baselibrary.http.response;

import android.content.Context;

import com.aslan.baselibrary.http.BaseError;

import io.reactivex.Completable;

/**
 * 处理业务异常
 *
 * @author Aslan chenhengfei@yy.com
 * @date 2020/6/12
 */
public class CompletableResponseFunction<T> extends BaseResponseFunction<T, Completable> {

    public CompletableResponseFunction(Context context) {
        super(context);
    }

    @Override
    Completable error(BaseError ex) {
        return Completable.error(ex);
    }

    @Override
    Completable just(T item) {
        return Completable.complete();
    }
}
