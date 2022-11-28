package com.aslan.baselibrary.http.response

import android.content.Context
import io.reactivex.Maybe

/**
 * 处理业务异常
 *
 * @author Aslan chenhengfei@yy.com
 * @date 2020/6/12
 */
class ResponseFunctionMaybe<T>(context: Context?) : BaseResponseFunction<T, Maybe<T>?>(
    context!!
) {
    override fun error(ex: Exception): Maybe<T> {
        return Maybe.error(ex)
    }

    override fun handleData(item: T?): Maybe<T> {
        return if (item == null) {
            Maybe.empty()
        } else {
            Maybe.just(item)
        }
    }
}