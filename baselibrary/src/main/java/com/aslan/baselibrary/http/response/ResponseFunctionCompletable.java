package com.aslan.baselibrary.http.response;

import android.content.Context;

import com.aslan.baselibrary.http.BaseError;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.reactivex.Completable;

/**
 * 处理业务异常
 *
 * @author Aslan chenhengfei@yy.com
 * @date 2020/6/12
 */
public class ResponseFunctionCompletable<T> extends BaseResponseFunction<T, Completable> {

    public ResponseFunctionCompletable(Context context) {
        super(context);
    }

    @Override
    Completable error(@NonNull BaseError ex) {
        return Completable.error(ex);
    }

    @Override
    Completable handleData(@Nullable T item) {
        return Completable.complete();
    }
}
