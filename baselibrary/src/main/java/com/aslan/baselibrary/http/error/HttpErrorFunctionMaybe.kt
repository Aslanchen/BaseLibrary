package com.aslan.baselibrary.http.error

import android.content.Context
import io.reactivex.Maybe

/**
 * 处理http异常
 *
 * @author Aslan chenhengfei@yy.com
 * @date 2020/6/10
 */
open class HttpErrorFunctionMaybe<T>(context: Context) : BaseHttpErrorFunction<Maybe<T>>(context) {
    override fun error(ex: Exception): Maybe<T> {
        return Maybe.error(ex)
    }
}