package com.aslan.baselibrary.http.error;

import android.content.Context;
import android.net.ParseException;
import android.util.Log;
import androidx.annotation.NonNull;
import com.aslan.baselibrary.R;
import com.aslan.baselibrary.error.ClientException;
import com.aslan.baselibrary.error.RemoteException;
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
public abstract class BaseHttpErrorFunction<T> implements Function<Throwable, T> {

  private Context context;

  public BaseHttpErrorFunction(Context context) {
    this.context = context;
  }

  @Override
  public T apply(@NonNull Throwable throwable) throws Exception {
    Exception ex;
    if (throwable instanceof UnknownHostException) {
      ex = new ClientException(context.getString(R.string.error_net_no_net), throwable);
    } else if (throwable instanceof ConnectException
        || throwable instanceof ConnectTimeoutException) {
      ex = new ClientException(context.getString(R.string.error_net_connect_timeout), throwable);
    } else if (throwable instanceof SocketTimeoutException) {
      ex = new ClientException(context.getString(R.string.error_net_socket_timeout), throwable);
    } else if (throwable instanceof HttpException) {
      ex = new ClientException(context.getString(R.string.error_net), throwable);
    } else if (throwable instanceof JsonParseException
        || throwable instanceof JSONException
        || throwable instanceof ParseException) {
      ex = new ClientException(context.getString(R.string.error_net), throwable);
    } else if (throwable instanceof SQLException) {
      ex = new ClientException(context.getString(R.string.error_local_database_default), throwable);
    } else if (throwable instanceof ClientException) {
      ex = (ClientException) throwable;
    } else if (throwable instanceof RemoteException) {
      ex = (RemoteException) throwable;
    } else {
      ex = new ClientException(context.getString(R.string.error_net), throwable);
    }

    Log.e(NetManager.TAG_LOG, "Http Error, ", throwable);
    return error(ex);
  }

  public abstract T error(Exception ex);
}
