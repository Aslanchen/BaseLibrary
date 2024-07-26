package com.aslan.baselibrary.http.response

import android.content.Context
import com.aslan.baselibrary.R
import io.reactivex.Flowable

/**
 * 处理业务异常
 *
 * @author Aslan chenhengfei@yy.com
 * @date 2020/6/12
 */
class ResponseFunctionFlowable<T>(val context: Context) : BaseResponseFunction<T, Flowable<T>>(context) {
    override fun error(ex: Exception): Flowable<T> {
        return Flowable.error(ex)
    }

    override fun handleData(item: T?): Flowable<T> {
        return if (item == null) {
            Flowable.error(NullPointerException(context.getString(R.string.error_data_empty)))
        } else {
            Flowable.just(item)
        }
    }
}