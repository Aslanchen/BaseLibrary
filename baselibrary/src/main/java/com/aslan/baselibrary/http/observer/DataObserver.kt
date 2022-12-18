package com.aslan.baselibrary.http.observer

import android.content.Context
import io.reactivex.CompletableObserver
import io.reactivex.disposables.Disposable
import io.reactivex.MaybeObserver
import io.reactivex.Observer
import io.reactivex.SingleObserver

/**
 * 网络回调基类，主要负责异常封装，以及数据序列化。
 *
 * @author Aslan
 * @date 2019/9/23
 */
abstract class DataObserver<T>(private val context: Context) : Observer<T> {
    override fun onError(e: Throwable) {}
    override fun onComplete() {}
    override fun onSubscribe(d: Disposable) {}
    override fun onNext(t: T) {
        handleSuccess(t)
    }

    abstract fun handleSuccess(t: T)
}