package com.aslan.baselibrary.http.response

import android.content.Context
import io.reactivex.Flowable

/**
 * 处理业务异常
 *
 * @author Aslan chenhengfei@yy.com
 * @date 2020/6/12
 */
class ResponseFunctionFlowable<T>(context: Context?) : BaseResponseFunction<T, Flowable<T>?>(
    context!!
) {
    override fun error(ex: Exception): Flowable<T> {
        return Flowable.error(ex)
    }

    override fun handleData(item: T?): Flowable<T> {
        return if (item == null) {
            Flowable.error(NullPointerException("respone data is empty"))
        } else {
            Flowable.just(item)
        }
    }
}