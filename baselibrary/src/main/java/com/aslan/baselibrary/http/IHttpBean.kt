package com.aslan.baselibrary.http

/**
 * 基础错误类
 *
 * @author Aslan
 * @date 2019/9/23
 */
interface IHttpBean<T> {
    val code: String
    val message: String
    val isSuccessful: Boolean
    val isTokenError: Boolean
    val data: T
}