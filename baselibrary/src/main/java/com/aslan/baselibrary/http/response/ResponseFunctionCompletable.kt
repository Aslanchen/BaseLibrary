package com.aslan.baselibrary.http.response

import android.content.Context
import io.reactivex.Completable

/**
 * 处理业务异常
 *
 * @author Aslan chenhengfei@yy.com
 * @date 2020/6/12
 */
class ResponseFunctionCompletable<T>(context: Context?) : BaseResponseFunction<T, Completable?>(
    context!!
) {
    override fun error(ex: Exception): Completable {
        return Completable.error(ex)
    }

    override fun handleData(item: T?): Completable {
        return Completable.complete()
    }
}