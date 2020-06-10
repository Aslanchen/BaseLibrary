package com.aslan.baselibrary.http;

import android.content.Context;

import com.aslan.baselibrary.R;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * 处理业务异常
 *
 * @author Aslan chenhengfei@yy.com
 * @date 2020/6/10
 */
public class ResponseFunction<T> implements Function<IHttpBean<T>, Observable<T>> {
    private Context context;

    public ResponseFunction(Context context) {
        this.context = context;
    }

    @Override
    public Observable<T> apply(IHttpBean<T> respone) throws Exception {
        if (respone.isTokenError()) {
            return Observable.error(new BaseError(NetManager.ERROR_TOKEN_ERROR,
                    context.getString(R.string.error_net_token_error)));
        }

        if (respone.isSuccessful()) {
            return Observable.just(respone.getData());
        } else {
            return Observable.error(new BaseError(respone.getCode(), respone.getMessage()));
        }
    }
}
