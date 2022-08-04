package com.aslan.baselibrary.base

import android.view.View
import com.aslan.baselibrary.error.TokenException
import com.aslan.baselibrary.listener.IBaseView
import io.reactivex.Completable
import io.reactivex.CompletableSource
import io.reactivex.CompletableTransformer
import java.util.concurrent.CancellationException

class DataCompletableTransformer(
    private val mBaseView: IBaseView,
    private val clickView: View? = null,
    private val isShowProgressbar: Boolean = true,
) :
    CompletableTransformer {

    private fun doOnSubscribe() {
        if (isShowProgressbar) {
            mBaseView.showProgressBar()
        }
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
        if (isShowProgressbar) {
            mBaseView.closeProgressBar()
        }
        clickView?.isEnabled = true
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