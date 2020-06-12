package com.aslan.baselibrary.http.response;

import android.content.Context;

import com.aslan.baselibrary.http.BaseError;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
    Maybe<T> error(@NonNull BaseError ex) {
        return Maybe.error(ex);
    }

    @Override
    Maybe<T> handleData(@Nullable T item) {
        if (item == null) {
            return Maybe.empty();
        } else {
            return Maybe.just(item);
        }
    }
}
