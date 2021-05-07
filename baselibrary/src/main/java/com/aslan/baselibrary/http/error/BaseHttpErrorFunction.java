package com.aslan.baselibrary.http.error;

import android.content.Context;
import android.net.ParseException;
import androidx.annotation.NonNull;
import com.aslan.baselibrary.R;
import com.aslan.baselibrary.http.BaseError;
import com.aslan.baselibrary.http.NetManager;
import com.google.gson.JsonParseException;
import io.reactivex.functions.Function;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.sql.SQLException;
import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;
import retrofit2.HttpException;

/**
 * 基础转换
 *
 * @author Aslan chenhengfei@yy.com
 * @date 2020/6/12
 */
abstract class BaseHttpErrorFunction<T> implements Function<Throwable, T> {

  private Context context;

  public BaseHttpErrorFunction(Context context) {
    this.context = context;
  }

  @Override
  public T apply(@NonNull Throwable throwable) throws Exception {
    BaseError ex;
    if (throwable instanceof UnknownHostException) {
      ex = new BaseError(NetManager.ERROR_NET_UNKNOWHOST,
          context.getString(R.string.error_net_no_net));
    } else if (throwable instanceof ConnectException
        || throwable instanceof ConnectTimeoutException) {
      ex = new BaseError(NetManager.ERROR_NET_CONNECT_TIMEOUT,
          context.getString(R.string.error_net_connect_timeout));
    } else if (throwable instanceof SocketTimeoutException) {
      ex = new BaseError(NetManager.ERROR_NET_SOCKET_TIMEOUT,
          context.getString(R.string.error_net_socket_timeout));
    } else if (throwable instanceof HttpException) {
      ex = new BaseError(NetManager.ERROR_NET_SERVER, context.getString(R.string.error_net));
    } else if (throwable instanceof JsonParseException
        || throwable instanceof JSONException
        || throwable instanceof ParseException) {
      ex = new BaseError(NetManager.ERROR_PARSE_DATA_ERROR,
          context.getString(R.string.error_net));
    } else if (throwable instanceof SQLException) {
      ex = new BaseError(NetManager.ERROR_DB,
          context.getString(R.string.error_local_database_default));
    } else {
      ex = new BaseError(NetManager.ERROR_OTHER, context.getString(R.string.error_net));
    }
    return error(ex);
  }

  abstract T error(BaseError ex);
}
