package com.aslan.baselibrary.http;

/**
 * HTTP基础错误类
 *
 * @author Aslan
 * @date 2019/9/23
 */
public class BaseHttpError extends Exception {

  private int code;

  public BaseHttpError(int code, String message) {
    super(message);
    this.code = code;
  }

  public BaseHttpError(int code, String message, Throwable e) {
    super(message);
    this.code = code;
  }

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }
}
