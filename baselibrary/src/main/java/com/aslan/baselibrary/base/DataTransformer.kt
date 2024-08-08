package com.aslan.baselibrary.base

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import com.aslan.baselibrary.R
import com.aslan.baselibrary.exception.TokenException
import com.aslan.baselibrary.listener.IBaseView
import io.reactivex.Flowable
import io.reactivex.FlowableTransformer
import io.reactivex.Maybe
import io.reactivex.MaybeSource
import io.reactivex.MaybeTransformer
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import io.reactivex.Single
import io.reactivex.SingleSource
import io.reactivex.SingleTransformer
import org.reactivestreams.Publisher
import java.util.concurrent.CancellationException

/**
 * 数据转换器，用于处理数据，如显示进度条，显示错误信息等
 *
 * @param mBaseView BaseView
 * @param clickView 点击的View，用于防止重复点击
 * @param isShowProgressbar 是否显示进度条
 * @param isShowToast 是否显示错误信息
 * @param progressbarMsg 进度条显示的信息
 * @param progressbarMsgResId 进度条显示的信息的资源id
 * @param progressbarCanCancel 进度条是否可以取消
 *
 * @author Aslan
 * @date 2023/04/06
 */
open class DataTransformer<T>(
    private val mBaseView: IBaseView,
    private val clickView: View? = null,
    private val isShowProgressbar: Boolean = true,
    private val isShowToast: Boolean = true,
    private val progressbarMsg: String? = null,
    private val progressbarMsgResId: Int? = null,
    private val progressbarCanCancel: Boolean = false,
) :
    ObservableTransformer<T, T>, FlowableTransformer<T, T>, SingleTransformer<T, T>,
    MaybeTransformer<T, T> {

    private var progressDialog: WaitingDialog? = null

    private fun getFragmentManager(): FragmentManager? {
        if (mBaseView is Fragment) {
            return mBaseView.parentFragmentManager
        } else if (mBaseView is AppCompatActivity) {
            return mBaseView.supportFragmentManager
        }
        return null
    }

    private fun doOnSubscribe() {
        if (isShowProgressbar) {
            val manager = getFragmentManager()
            if (manager != null) {
                val waitingDialogBuilder = WaitingDialog.Builder(mBaseView.requireContext())
                waitingDialogBuilder.setCancelable(progressbarCanCancel)
                if (progressbarMsgResId != null) {
                    waitingDialogBuilder.setMessage(progressbarMsgResId)
                }

                if (progressbarMsg != null) {
                    waitingDialogBuilder.setMessage(progressbarMsg)
                }

                mBaseView.getLifecycleOwner().lifecycleScope.launchWhenResumed {
                    this@DataTransformer.progressDialog = waitingDialogBuilder.show(manager)
                }
            }
        }

        clickView?.isEnabled = false
    }

    private fun doOnError(error: Throwable) {
        if (isShowProgressbar) {
            try {
                progressDialog?.dismiss()
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
        clickView?.isEnabled = true

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
            try {
                progressDialog?.dismiss()
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
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
}