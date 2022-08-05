package com.aslan.baselibrary.exception;

/**
 * 业务服务器异常
 *
 * @author Aslan
 * @date 2019/9/23
 */
public class RemoteException extends Exception {

  private String code;

  public RemoteException(String code, String message) {
    super(message);
    this.code = code;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }
}
