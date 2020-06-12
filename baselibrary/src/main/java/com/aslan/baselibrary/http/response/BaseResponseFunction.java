package com.aslan.baselibrary.http.response;

import android.content.Context;

import com.aslan.baselibrary.http.BaseError;
import com.aslan.baselibrary.http.IHttpBean;
import com.aslan.baselibrary.http.NetManager;

import io.reactivex.functions.Function;

/**
 * 处理业务异常
 *
 * @author Aslan chenhengfei@yy.com
 * @date 2020/6/12
 */
abstract class BaseResponseFunction<T, R> implements Function<IHttpBean<T>, R> {

    private Context context;

    public BaseResponseFunction(Context context) {
        this.context = context;
    }

    @Override
    public R apply(IHttpBean<T> respone) throws Exception {
        if (respone.isTokenError()) {
            return error(new BaseError(NetManager.ERROR_TOKEN_ERROR,
                    context.getString(com.aslan.baselibrary.R.string.error_net_token_error)));
        }

        if (respone.isSuccessful()) {
            return just(respone.getData());
        } else {
            return error(new BaseError(respone.getCode(), respone.getMessage()));
        }
    }

    abstract R error(BaseError ex);

    abstract R just(T item);
}
