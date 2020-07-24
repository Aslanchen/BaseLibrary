package com.aslan.baselibrary.http;

/**
 * Token 异常
 *
 * @author Aslan 122560007@163.com
 * @since 2020/7/24
 */
public class EventTokenError {

  private int errorCode;

  public EventTokenError(int errorCode) {
    this.errorCode = errorCode;
  }

  public int getErrorCode() {
    return errorCode;
  }

  public void setErrorCode(int errorCode) {
    this.errorCode = errorCode;
  }
}
