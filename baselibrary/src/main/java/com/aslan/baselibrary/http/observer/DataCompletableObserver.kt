package com.aslan.baselibrary.http.observer

import android.content.Context
import io.reactivex.CompletableObserver
import io.reactivex.disposables.Disposable

/**
 * 网络回调基类，主要负责异常封装，以及数据序列化。
 *
 * @author Aslan
 * @date 2019/9/23
 */
abstract class DataCompletableObserver(private val context: Context) : CompletableObserver {
    override fun onError(e: Throwable) {}
    override fun onComplete() {
        handleSuccess()
    }

    override fun onSubscribe(d: Disposable) {}
    abstract fun handleSuccess()
}