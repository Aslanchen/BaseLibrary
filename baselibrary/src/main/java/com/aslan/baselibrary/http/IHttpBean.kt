package com.aslan.baselibrary.http

/**
 * 基础错误类
 *
 * @author Aslan
 * @date 2019/9/23
 */
interface IHttpBean<T> {
    fun getCode(): String
    fun getMessage(): String
    fun isSuccessful(): Boolean
    fun isTokenError(): Boolean
    fun getData(): T?
}