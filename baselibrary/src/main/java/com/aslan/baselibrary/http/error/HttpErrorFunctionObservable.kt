package com.aslan.baselibrary.http.error

import android.content.Context
import io.reactivex.Observable

/**
 * 处理http异常
 *
 * @author Aslan chenhengfei@yy.com
 * @date 2020/6/10
 */
open class HttpErrorFunctionObservable<T>(context: Context) :
    BaseHttpErrorFunction<Observable<T>>(context) {
    override fun error(ex: Exception): Observable<T> {
        return Observable.error(ex)
    }
}