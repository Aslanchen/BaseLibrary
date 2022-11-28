package com.aslan.baselibrary.http

/**
 * 基础错误类
 *
 * @author Aslan
 * @date 2019/9/23
 */
interface IHttpBean<T> {
    open val code: String
    open val message: String
    open val isSuccessful: Boolean
    open val isTokenError: Boolean
    open val data: T
}