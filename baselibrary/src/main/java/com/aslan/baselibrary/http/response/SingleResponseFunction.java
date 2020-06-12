package com.aslan.baselibrary.http.response;

import android.content.Context;

import com.aslan.baselibrary.http.BaseError;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
    Single<T> error(@NonNull BaseError ex) {
        return Single.error(ex);
    }

    @Override
    Single<T> handleData(@Nullable T item) {
        if (item == null) {
            return Single.error(new NullPointerException("respone data is empty"));
        } else {
            return Single.just(item);
        }
    }
}
