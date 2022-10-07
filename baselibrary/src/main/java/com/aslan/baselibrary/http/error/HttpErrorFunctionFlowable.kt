package com.aslan.baselibrary.http.error

import android.content.Context
import io.reactivex.Flowable

/**
 * 处理http异常
 *
 * @author Aslan chenhengfei@yy.com
 * @date 2020/6/10
 */
open class HttpErrorFunctionFlowable<T>(context: Context) :
    BaseHttpErrorFunction<Flowable<T>>(context) {
    override fun error(ex: Exception): Flowable<T> {
        return Flowable.error(ex)
    }
}