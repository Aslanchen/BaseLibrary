package com.aslan.baselibrary.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.coroutineScope
import com.aslan.baselibrary.listener.IBaseView
import com.aslan.baselibrary.listener.IMVPBasePresenter

/**
 * MPV基础类
 *
 * @author Aslan
 * @date 2018/4/11
 */
abstract class MVPBasePresenter<V : IBaseView> : IMVPBasePresenter {
    var mView: V
    var activity: BaseActivity
    var fragment: BaseFragment? = null

    constructor(activity: BaseActivity) {
        mView = activity as V
        this.activity = activity
    }

    constructor(fragment: BaseFragment) {
        mView = fragment as V
        this.fragment = fragment
        activity = fragment.activity as BaseActivity
    }

    fun requireContext(): Context {
        return mView.requireContext()
    }

    override fun iniBundle(bundle: Bundle) {}
    override fun onCreate(owner: LifecycleOwner) {}
    override fun iniData() {}
    override fun onStart(owner: LifecycleOwner) {}
    override fun onResume(owner: LifecycleOwner) {}
    override fun onPause(owner: LifecycleOwner) {}
    override fun onStop(owner: LifecycleOwner) {}
    override fun onDestroy(owner: LifecycleOwner) {}
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {}

    override fun getFragmentManager(): FragmentManager {
        return if (fragment != null) {
            fragment!!.parentFragmentManager
        } else {
            activity.supportFragmentManager
        }
    }

    /**
     * ViewModelProvider 使用
     */
    protected val viewModelProvider: ViewModelProvider
        protected get() = if (fragment != null) {
            ViewModelProvider(fragment!!)
        } else {
            ViewModelProvider(activity)
        }

    /**
     * ViewModelProvider 使用
     */
    protected fun getViewModelProvider(factory: ViewModelProvider.Factory): ViewModelProvider {
        return if (fragment != null) {
            ViewModelProvider(fragment!!, factory)
        } else {
            ViewModelProvider(activity, factory)
        }
    }

    val lifecycleOwner: LifecycleOwner
        get() = mView.getLifecycleOwner()

    val lifecycle: Lifecycle
        get() = lifecycleOwner.lifecycle

    val lifecycleScope: LifecycleCoroutineScope
        get() = lifecycle.coroutineScope
}