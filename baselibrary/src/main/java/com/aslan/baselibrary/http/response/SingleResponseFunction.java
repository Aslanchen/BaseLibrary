package com.aslan.baselibrary.http.response;

import android.content.Context;

import com.aslan.baselibrary.http.BaseError;

import io.reactivex.Single;

/**
 * 处理业务异常
 *
 * @author Aslan chenhengfei@yy.com
 * @date 2020/6/12
 */
public class SingleResponseFunction<T> extends BaseResponseFunction<T, Single<T>> {

    public SingleResponseFunction(Context context) {
        super(context);
    }

    @Override
    Single<T> error(BaseError ex) {
        return Single.error(ex);
    }

    @Override
    Single<T> just(T item) {
        return Single.just(item);
    }
}
