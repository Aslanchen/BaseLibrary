package com.aslan.baselibrary.http;

import android.content.Context;
import android.net.ParseException;

import com.aslan.baselibrary.R;
import com.google.gson.JsonParseException;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import retrofit2.HttpException;

/**
 * 网络回调基类，主要负责异常封装，以及数据序列化。
 *
 * @author Aslan
 * @date 2019/9/23
 */
public abstract class BaseObserver<T extends IHttpBean> implements SingleObserver<T> {

    private Context context;

    public static final int ERROR_NET_SERVER = -100;
    public static final int ERROR_NO_NET = -101;
    public static final int ERROR_NET_UNKNOWHOST = -103;
    public static final int ERROR_NET_CONNECT_TIMEOUT = -104;
    public static final int ERROR_NET_SOCKET_TIMEOUT = -105;
    public static final int ERROR_PARSE_DATA_ERROR = -106;
    public static final int ERROR_OTHER = -107;
    public static final int ERROR_TOKEN_ERROR = -108;

    public BaseObserver(Context context) {
        this.context = context;
    }

    @Override
    public void onError(Throwable e) {
        handleError(tranlateError(e));
    }

    private BaseError tranlateError(Throwable e) {
        BaseError ex;
        if (e instanceof UnknownHostException) {
            ex = new BaseError(ERROR_NET_UNKNOWHOST, context.getString(R.string.error_net_no_net));
        } else if (e instanceof ConnectException
                || e instanceof ConnectTimeoutException) {
            ex = new BaseError(ERROR_NET_CONNECT_TIMEOUT,
                    context.getString(R.string.error_net_connect_timeout));
        } else if (e instanceof SocketTimeoutException) {
            ex = new BaseError(ERROR_NET_SOCKET_TIMEOUT,
                    context.getString(R.string.error_net_socket_timeout));
        } else if (e instanceof HttpException) {
            ex = new BaseError(ERROR_NET_SERVER, context.getString(R.string.error_net));
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {
            ex = new BaseError(ERROR_PARSE_DATA_ERROR, context.getString(R.string.error_net));
        } else {
            ex = new BaseError(ERROR_OTHER, context.getString(R.string.error_net));
        }
        return ex;
    }

    @Override
    public void onSubscribe(Disposable d) {
        if (!NetManager.isNetworkAvailable(context)) {
            handleError(new BaseError(ERROR_NO_NET, context.getString(R.string.error_net_no_net)));
        }
    }

    @Override
    public void onSuccess(T t) {
        if (t.isTokenError()) {
            handleError(new BaseError(ERROR_TOKEN_ERROR,
                    context.getString(R.string.error_net_token_error)));
            return;
        }

        if (t.isSuccessful()) {
            handleSuccess(t);
        } else {
            handleError(new BaseError(t.getCode(), t.getMessage()));
        }
    }

    public abstract void handleError(BaseError e);

    public abstract void handleSuccess(T t);

}