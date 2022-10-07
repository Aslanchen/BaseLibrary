package com.aslan.baselibrary.http.error

import android.content.Context
import io.reactivex.Single

/**
 * 处理http异常
 *
 * @author Aslan chenhengfei@yy.com
 * @date 2020/6/10
 */
open class HttpErrorFunctionSingle<T>(context: Context) :
    BaseHttpErrorFunction<Single<T>>(context) {
    override fun error(ex: Exception): Single<T> {
        return Single.error(ex)
    }
}