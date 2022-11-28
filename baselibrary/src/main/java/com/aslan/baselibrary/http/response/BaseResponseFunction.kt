package com.aslan.baselibrary.http.response

import android.content.Context
import com.aslan.baselibrary.exception.BusinessException
import com.aslan.baselibrary.exception.TokenException
import com.aslan.baselibrary.http.IHttpBean
import io.reactivex.functions.Function

/**
 * 处理业务异常
 *
 * @author Aslan chenhengfei@yy.com
 * @date 2020/6/12
 */
abstract class BaseResponseFunction<T, R>(private val context: Context) :
    Function<IHttpBean<T>, R> {

    @Throws(Exception::class)
    override fun apply(respone: IHttpBean<T>): R {
        if (respone.isTokenError()) {
            return error(TokenException(respone.getCode(), respone.getMessage()))
        }
        return if (respone.isSuccessful()) {
            handleData(respone.getData())
        } else {
            error(BusinessException(respone.getCode(), respone.getMessage()))
        }
    }

    abstract fun error(ex: Exception): R
    abstract fun handleData(item: T?): R
}