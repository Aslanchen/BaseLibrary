package com.aslan.baselibrary.listener

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.annotation.RequiresPermission
import androidx.annotation.StringRes
import androidx.annotation.UiThread
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.trello.rxlifecycle3.LifecycleProvider

/**
 * 错误提示类以及等待框
 *
 * @author Aslan
 * @date 2018/4/11
 */
@UiThread
interface IBaseView {
    fun getLifecycleProvider(): LifecycleProvider<Lifecycle.Event>
    fun getLifecycleOwner(): LifecycleOwner
    fun requireContext(): Context
    fun showProgressBar()
    fun showProgressBar(@StringRes msg: Int)
    fun showProgressBar(msg: String)
    fun showProgressBar(canCancel: Boolean)
    fun showProgressBar(canCancel: Boolean, @StringRes msg: Int)
    fun showProgressBar(canCancel: Boolean, msg: String)
    fun closeProgressBar()
    fun showToastMessage(@StringRes resId: Int)
    fun showToastMessage(text: CharSequence)
    fun showToastMessage(@StringRes resId: Int, duration: Int)
    fun showToastMessage(text: CharSequence, duration: Int)
    fun startActivity(intent: Intent)
    fun startActivityForResult(@RequiresPermission intent: Intent, requestCode: Int)
    fun startActivityForResult(
        @RequiresPermission intent: Intent,
        requestCode: Int,
        options: Bundle?
    )

    fun thisFinish()
}