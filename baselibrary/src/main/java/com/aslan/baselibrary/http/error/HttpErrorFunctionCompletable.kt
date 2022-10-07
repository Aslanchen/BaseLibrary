package com.aslan.baselibrary.http.error

import android.content.Context
import io.reactivex.Completable

/**
 * 处理http异常
 *
 * @author Aslan chenhengfei@yy.com
 * @date 2020/6/10
 */
open class HttpErrorFunctionCompletable(context: Context) :
    BaseHttpErrorFunction<Completable>(context) {
    override fun error(ex: Exception): Completable {
        return Completable.error(ex)
    }
}