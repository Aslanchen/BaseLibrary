package com.aslan.baselibrary.base

import android.view.View
import com.aslan.baselibrary.error.TokenException
import com.aslan.baselibrary.listener.IBaseView
import io.reactivex.*
import org.reactivestreams.Publisher
import java.util.concurrent.CancellationException

class DataTransformer<T>(private val mBaseView: IBaseView, private val clickView: View? = null) :
    ObservableTransformer<T, T>, FlowableTransformer<T, T>, SingleTransformer<T, T>,
    MaybeTransformer<T, T>, CompletableTransformer {

    private fun doOnSubscribe() {
        mBaseView.showProgressBar()
        clickView?.isEnabled = false
    }

    private fun doOnError(error: Throwable) {
        if (error is CancellationException) {
            //Rxjava绑定生命周期后，会触发此异常
            return@doOnError
        }

        if (error is TokenException) {
            //token异常，走统一处理
            return@doOnError
        }

        if (error.message.isNullOrEmpty()) {
            mBaseView.showToastMessage("出现未知错误")
        } else {
            mBaseView.showToastMessage(error.message!!)
        }
    }

    private fun doFinally() {
        mBaseView.closeProgressBar()
        clickView?.isEnabled = true
    }

    override fun apply(upstream: Observable<T>): ObservableSource<T> {
        return upstream
            .doOnSubscribe {
                doOnSubscribe()
            }
            .doOnError { error ->
                doOnError(error)
            }
            .doFinally {
                doFinally()
            }
    }

    override fun apply(upstream: Flowable<T>): Publisher<T> {
        return upstream
            .doOnSubscribe {
                doOnSubscribe()
            }
            .doOnError { error ->
                doOnError(error)
            }
            .doFinally {
                doFinally()
            }
    }

    override fun apply(upstream: Single<T>): SingleSource<T> {
        return upstream
            .doOnSubscribe {
                doOnSubscribe()
            }
            .doOnError { error ->
                doOnError(error)
            }
            .doFinally {
                doFinally()
            }
    }

    override fun apply(upstream: Maybe<T>): MaybeSource<T> {
        return upstream
            .doOnSubscribe {
                doOnSubscribe()
            }
            .doOnError { error ->
                doOnError(error)
            }
            .doFinally {
                doFinally()
            }
    }

    override fun apply(upstream: Completable): CompletableSource {
        return upstream
            .doOnSubscribe {
                doOnSubscribe()
            }
            .doOnError { error ->
                doOnError(error)
            }
            .doFinally {
                doFinally()
            }
    }
}