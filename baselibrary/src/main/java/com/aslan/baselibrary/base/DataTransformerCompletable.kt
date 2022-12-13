package com.aslan.baselibrary.base

import android.view.View
import com.aslan.baselibrary.R
import com.aslan.baselibrary.exception.TokenException
import com.aslan.baselibrary.listener.IBaseView
import io.reactivex.Completable
import io.reactivex.CompletableSource
import io.reactivex.CompletableTransformer
import java.util.concurrent.CancellationException

open class DataTransformerCompletable(
    private val mBaseView: IBaseView,
    private val clickView: View? = null,
    private val isShowProgressbar: Boolean = true,
    private val isShowToast: Boolean = true,
    private val progressbarMsg: String? = null,
    private val progressbarMsgResId: Int? = null,
    private val progressbarCanCancel: Boolean = false,
) :
    CompletableTransformer {

    private fun doOnSubscribe() {
        if (isShowProgressbar) {
            if (progressbarMsg != null) {
                mBaseView.showProgressBar(progressbarCanCancel, progressbarMsg)
            } else if (progressbarMsgResId != null) {
                mBaseView.showProgressBar(progressbarCanCancel, progressbarMsgResId)
            } else {
                mBaseView.showProgressBar(progressbarCanCancel)
            }
        }

        clickView?.isEnabled = false
    }

    private fun doOnError(error: Throwable) {
        if (error is CancellationException) {
            //Rxjava绑定生命周期后，会触发此异常
            return
        }

        if (error is TokenException) {
            //token异常，走统一处理
            return
        }

        if (isShowToast) {
            if (error.message.isNullOrEmpty()) {
                mBaseView.showToastMessage(R.string.base_data_error_unknow)
            } else {
                mBaseView.showToastMessage(error.message!!)
            }
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