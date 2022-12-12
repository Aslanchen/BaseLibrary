package com.aslan.baselibrary.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.annotation.Size
import androidx.annotation.StringRes
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.*
import com.aslan.baselibrary.listener.IBaseView
import com.aslan.baselibrary.listener.IMVPBasePresenter
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import com.vmadalin.easypermissions.models.PermissionRequest

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
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (this is EasyPermissions.PermissionCallbacks) {
            EasyPermissions
                .onRequestPermissionsResult(requestCode, permissions, grantResults, this)
        }
    }

    override fun getFragmentManager(): FragmentManager {
        return if (fragment != null) {
            fragment!!.parentFragmentManager
        } else {
            activity.supportFragmentManager
        }
    }

    /**
     * EasyPermissions 使用
     */
    protected fun somePermissionDenied(vararg perms: String): Boolean {
        return if (fragment == null) {
            EasyPermissions.somePermissionDenied(activity, *perms)
        } else {
            EasyPermissions.somePermissionDenied(fragment!!, *perms)
        }
    }

    /**
     * EasyPermissions 使用
     */
    protected fun requestPermissions(
        @StringRes rationaleResId: Int, requestCode: Int,
        @Size(min = 1) vararg perms: String
    ) {
        requestPermissions(requireContext().getString(rationaleResId), requestCode, *perms)
    }

    /**
     * EasyPermissions 使用
     */
    protected fun requestPermissions(
        rationale: String, requestCode: Int,
        @Size(min = 1) vararg perms: String
    ) {
        if (fragment == null) {
            EasyPermissions.requestPermissions(activity, rationale, requestCode, *perms)
        } else {
            EasyPermissions.requestPermissions(fragment!!, rationale, requestCode, *perms)
        }
    }

    /**
     * EasyPermissions 使用
     */
    protected fun newPermissionRequestBuilder(
        requestCode: Int,
        @Size(min = 1) vararg perms: String
    ): PermissionRequest.Builder {
        return PermissionRequest.Builder(requireContext())
            .code(requestCode)
            .perms(perms)
    }

    /**
     * EasyPermissions 使用,进入设置界面
     */
    protected fun newAppSettingsDialogBuilder(): SettingsDialog.Builder {
        return SettingsDialog.Builder(requireContext())
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