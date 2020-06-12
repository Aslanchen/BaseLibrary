package com.aslan.baselibrary.http.response;

import android.content.Context;

import com.aslan.baselibrary.http.BaseError;

import io.reactivex.Flowable;

/**
 * 处理业务异常
 *
 * @author Aslan chenhengfei@yy.com
 * @date 2020/6/12
 */
public class FlowableResponseFunction<T> extends BaseResponseFunction<T, Flowable<T>> {

    public FlowableResponseFunction(Context context) {
        super(context);
    }

    @Override
    Flowable<T> error(BaseError ex) {
        return Flowable.error(ex);
    }

    @Override
    Flowable<T> just(T item) {
        return Flowable.just(item);
    }
}
