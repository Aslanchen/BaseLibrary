package com.aslan.baselibrary.base;

/**
 * 基础错误类
 *
 * @author Aslan
 * @date 2019/9/23
 */
public class DataError extends Exception {

  public static final int ERROR_UNKONOW = -999;
  public static final int ERROR_DB = -200;//SQLException
  public static final int ERROR_HTTP_NET_SERVER = -100;//HttpException
  public static final int ERROR_HTTP_NO_NET = -101;//没有网络
  public static final int ERROR_HTTP_NET_UNKNOWHOST = -103;//UnknownHostException
  public static final int ERROR_HTTP_NET_CONNECT_TIMEOUT = -104;//ERROR_NET_CONNECT_TIMEOUT
  public static final int ERROR_HTTP_NET_SOCKET_TIMEOUT = -105;//ERROR_NET_SOCKET_TIMEOUT
  public static final int ERROR_HTTP_PARSE_DATA_ERROR = -106;//JsonParseException
  public static final int ERROR_HTTP_OTHER = -107;//服务器异常
  public static final int ERROR_HTTP_TOKEN_ERROR = -108;//Token异常

  private int code;

  public DataError(int code, String message) {
    super(message);
    this.code = code;
  }

  public DataError(Throwable cause) {
    super(cause);
    this.code = ERROR_UNKONOW;
  }

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }
}
