package com.aslan.baselibrary.http.response;

import android.content.Context;

import com.aslan.baselibrary.http.BaseError;

import io.reactivex.Maybe;

/**
 * 处理业务异常
 *
 * @author Aslan chenhengfei@yy.com
 * @date 2020/6/12
 */
public class MaybeResponseFunction<T> extends BaseResponseFunction<T, Maybe<T>> {

    public MaybeResponseFunction(Context context) {
        super(context);
    }

    @Override
    Maybe<T> error(BaseError ex) {
        return Maybe.error(ex);
    }

    @Override
    Maybe<T> just(T item) {
        return Maybe.just(item);
    }
}
