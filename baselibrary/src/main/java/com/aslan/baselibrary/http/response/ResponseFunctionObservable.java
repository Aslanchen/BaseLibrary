package com.aslan.baselibrary.http.response;

import android.content.Context;

import com.aslan.baselibrary.http.BaseError;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.reactivex.Observable;

/**
 * 处理业务异常
 *
 * @author Aslan chenhengfei@yy.com
 * @date 2020/6/12
 */
public class ResponseFunctionObservable<T> extends BaseResponseFunction<T, Observable<T>> {

    public ResponseFunctionObservable(Context context) {
        super(context);
    }

    @Override
    Observable<T> error(@NonNull BaseError ex) {
        return Observable.error(ex);
    }

    @Override
    Observable<T> handleData(@Nullable T item) {
        if (item == null) {
            return Observable.error(new NullPointerException("respone data is empty"));
        } else {
            return Observable.just(item);
        }
    }
}
