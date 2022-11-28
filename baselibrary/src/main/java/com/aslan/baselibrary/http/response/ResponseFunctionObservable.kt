package com.aslan.baselibrary.http.response

import android.content.Context
import io.reactivex.Observable

/**
 * 处理业务异常
 *
 * @author Aslan chenhengfei@yy.com
 * @date 2020/6/12
 */
class ResponseFunctionObservable<T>(context: Context?) : BaseResponseFunction<T, Observable<T>?>(
    context!!
) {
    override fun error(ex: Exception): Observable<T> {
        return Observable.error(ex)
    }

    override fun handleData(item: T?): Observable<T> {
        return if (item == null) {
            Observable.error(NullPointerException("respone data is empty"))
        } else {
            Observable.just(item)
        }
    }
}