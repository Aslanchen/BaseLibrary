package com.aslan.baselibrary.http.response

import android.content.Context
import io.reactivex.Single

/**
 * 处理业务异常
 *
 * @author Aslan chenhengfei@yy.com
 * @date 2020/6/12
 */
class ResponseFunctionSingle<T>(context: Context?) : BaseResponseFunction<T, Single<T>?>(
    context!!
) {
    override fun error(ex: Exception): Single<T> {
        return Single.error(ex)
    }

    override fun handleData(item: T?): Single<T> {
        return if (item == null) {
            Single.error(NullPointerException("respone data is empty"))
        } else {
            Single.just(item)
        }
    }
}