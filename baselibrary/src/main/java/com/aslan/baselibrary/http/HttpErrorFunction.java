package com.aslan.baselibrary.http;

import android.content.Context;
import android.net.ParseException;

import com.google.gson.JsonParseException;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import retrofit2.HttpException;

/**
 * 处理http异常
 *
 * @author Aslan chenhengfei@yy.com
 * @date 2020/6/10
 */
public class HttpErrorFunction<T> implements Function<Throwable, Observable<T>> {
    private Context context;

    public HttpErrorFunction(Context context) {
        this.context = context;
    }

    @Override
    public Observable<T> apply(Throwable throwable) throws Exception {
        BaseError ex;
        if (throwable instanceof UnknownHostException) {
            ex = new BaseError(NetManager.ERROR_NET_UNKNOWHOST,
                    context.getString(com.aslan.baselibrary.R.string.error_net_no_net));
        } else if (throwable instanceof ConnectException
                || throwable instanceof ConnectTimeoutException) {
            ex = new BaseError(NetManager.ERROR_NET_CONNECT_TIMEOUT,
                    context.getString(com.aslan.baselibrary.R.string.error_net_connect_timeout));
        } else if (throwable instanceof SocketTimeoutException) {
            ex = new BaseError(NetManager.ERROR_NET_SOCKET_TIMEOUT,
                    context.getString(com.aslan.baselibrary.R.string.error_net_socket_timeout));
        } else if (throwable instanceof HttpException) {
            ex = new BaseError(NetManager.ERROR_NET_SERVER,
                    context.getString(com.aslan.baselibrary.R.string.error_net));
        } else if (throwable instanceof JsonParseException
                || throwable instanceof JSONException
                || throwable instanceof ParseException) {
            ex = new BaseError(NetManager.ERROR_PARSE_DATA_ERROR,
                    context.getString(com.aslan.baselibrary.R.string.error_net));
        } else {
            ex = new BaseError(NetManager.ERROR_OTHER,
                    context.getString(com.aslan.baselibrary.R.string.error_net));
        }
        return Observable.error(ex);
    }
}
