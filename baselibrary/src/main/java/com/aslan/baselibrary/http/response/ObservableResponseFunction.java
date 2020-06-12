package com.aslan.baselibrary.http.response;

import android.content.Context;

import com.aslan.baselibrary.http.BaseError;

import io.reactivex.Observable;

/**
 * 处理业务异常
 *
 * @author Aslan chenhengfei@yy.com
 * @date 2020/6/12
 */
public class ObservableResponseFunction<T> extends BaseResponseFunction<T, Observable<T>> {

    public ObservableResponseFunction(Context context) {
        super(context);
    }

    @Override
    Observable<T> error(BaseError ex) {
        return Observable.error(ex);
    }

    @Override
    Observable<T> just(T item) {
        return Observable.just(item);
    }
}
