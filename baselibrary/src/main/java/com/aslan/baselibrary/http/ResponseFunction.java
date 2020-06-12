package com.aslan.baselibrary.http;

import android.content.Context;

import com.aslan.baselibrary.R;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;

/**
 * 处理业务异常
 *
 * @author Aslan chenhengfei@yy.com
 * @date 2020/6/10
 */
public class ResponseFunction<T> implements Function<IHttpBean<T>, Flowable<T>> {
    private Context context;

    public ResponseFunction(Context context) {
        this.context = context;
    }

    @Override
    public Flowable<T> apply(IHttpBean<T> respone) throws Exception {
        if (respone.isTokenError()) {
            return Flowable.error(new BaseError(NetManager.ERROR_TOKEN_ERROR,
                    context.getString(R.string.error_net_token_error)));
        }

        if (respone.isSuccessful()) {
            return Flowable.just(respone.getData());
        } else {
            return Flowable.error(new BaseError(respone.getCode(), respone.getMessage()));
        }
    }
}
