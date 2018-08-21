package com.aslan.baselibrary.http;

import android.content.Context;
import android.support.annotation.NonNull;
import com.aslan.baselibrary.R;
import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class BaseHttpError {

  public static final int ERROR_NET_SERVER = 100;
  public static final int ERROR_NO_NET = 101;
  public static final int ERROR_NET_UNKNOWHOST = 103;
  public static final int ERROR_NET_CONNECT_TIMEOUT = 104;
  public static final int ERROR_NET_SOCKET_TIMEOUT = 105;

  public static final int ERROR_DB_ERROR = 200;

  private int id;
  private int code;
  private String msg;

  public BaseHttpError(Context context) {
    code = BaseHttpError.ERROR_NET_SERVER;
    this.msg = context.getString(R.string.error_net);
  }

  public BaseHttpError(int httpErrorCode, String msg) {
    code = httpErrorCode;
    this.msg = msg;
  }

  public BaseHttpError(Context context, @NonNull IOException e) {
    if (e instanceof UnknownHostException) {
      code = BaseHttpError.ERROR_NET_UNKNOWHOST;
      msg = context.getString(R.string.error_net_no_net);
    } else if (e instanceof ConnectException) {
      code = BaseHttpError.ERROR_NET_CONNECT_TIMEOUT;
      msg = context.getString(R.string.error_net_connect_timeout);
    } else if (e instanceof SocketTimeoutException) {
      code = BaseHttpError.ERROR_NET_SOCKET_TIMEOUT;
      msg = context.getString(R.string.error_net_socket_timeout);
    } else {
      code = BaseHttpError.ERROR_NET_SERVER;
      msg = context.getString(R.string.error_net);
    }
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }
}
