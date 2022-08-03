package com.aslan.baselibrary.error;

/**
 * 客户端本地异常
 *
 * @author Aslan
 * @date 2019/9/23
 */
public class ClientException extends Exception {

  public ClientException(String message, Throwable cause) {
    super(message, cause);
  }
}
