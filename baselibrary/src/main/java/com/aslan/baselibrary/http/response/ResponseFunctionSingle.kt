package com.aslan.baselibrary.http.response

import android.content.Context
import com.aslan.baselibrary.R
import io.reactivex.Single

/**
 * 处理业务异常
 *
 * @author Aslan chenhengfei@yy.com
 * @date 2020/6/12
 */
class ResponseFunctionSingle<T>(val context: Context) : BaseResponseFunction<T, Single<T>>(context) {
    override fun error(ex: Exception): Single<T> {
        return Single.error(ex)
    }

    override fun handleData(item: T?): Single<T> {
        return if (item == null) {
            Single.error(NullPointerException(context.getString(R.string.error_data_empty)))
        } else {
            Single.just(item)
        }
    }
}