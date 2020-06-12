package com.aslan.baselibrary.http.error;

import android.content.Context;

import com.aslan.baselibrary.http.BaseError;

import io.reactivex.Single;

/**
 * 处理http异常
 *
 * @author Aslan chenhengfei@yy.com
 * @date 2020/6/10
 */
public class SingleHttpErrorFunction<T> extends BaseHttpErrorFunction<Single<T>> {

    public SingleHttpErrorFunction(Context context) {
        super(context);
    }

    @Override
    Single<T> error(BaseError ex) {
        return Single.error(ex);
    }
}
