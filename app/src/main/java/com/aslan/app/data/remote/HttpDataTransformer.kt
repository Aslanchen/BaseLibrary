package com.aslan.app.data.remote

import android.content.Context
import com.aslan.baselibrary.http.error.HttpErrorFunctionMaybe
import com.aslan.baselibrary.http.response.ResponseFunctionMaybe
import io.reactivex.Maybe
import io.reactivex.MaybeSource
import io.reactivex.MaybeTransformer

/**
 * HTTP数据转换
 *
 * @author Aslan
 * @date 2023/04/06
 */
open class HttpDataTransformer<T>(val context: Context, val interceptAuthentication: Boolean = true) : MaybeTransformer<HttpBaseModel<T>, T> {
    override fun apply(upstream: Maybe<HttpBaseModel<T>>): MaybeSource<T> {
        val r = upstream.flatMap(ResponseFunctionMaybe(context))
            .onErrorResumeNext(HttpErrorFunctionMaybe(context))
        if (interceptAuthentication == true) {
            r.retryWhen(TokenErrorFunction(context))
        }
        return r
    }
}