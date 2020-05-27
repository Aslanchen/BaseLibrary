package com.aslan.baselibrary.http;

/**
 * 基础错误类
 *
 * @author Aslan
 * @date 2019/9/23
 */
public interface IHttpBean {
    int getCode();

    String getMessage();

    boolean isSuccessful();

    boolean isTokenError();
}
