package com.aslan.baselibrary.http.observer

import android.content.Context
import io.reactivex.MaybeObserver
import io.reactivex.disposables.Disposable

/**
 * 网络回调基类，主要负责异常封装，以及数据序列化。
 *
 * @author Aslan
 * @date 2019/9/23
 */
abstract class DataMaybeObserver<T>(private val context: Context) : MaybeObserver<T> {
    override fun onError(e: Throwable) {}
    override fun onSubscribe(d: Disposable) {}
    override fun onComplete() {
        handleSuccess(null)
    }

    override fun onSuccess(t: T) {
        handleSuccess(t)
    }

    abstract fun handleSuccess(t: T?)
}