package com.aslan.baselibrary.base

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.MainThread
import androidx.annotation.StringRes
import androidx.annotation.UiThread
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.aslan.baselibrary.R
import com.aslan.baselibrary.listener.IBaseView
import com.aslan.baselibrary.view.CustomToolbar
import com.trello.lifecycle2.android.lifecycle.AndroidLifecycle
import com.trello.rxlifecycle3.LifecycleProvider
import com.vmadalin.easypermissions.EasyPermissions
import java.lang.Deprecated

/**
 * 基础类
 *
 * @author Aslan
 * @date 2018/4/11
 */
abstract class BaseFragment : Fragment(), IBaseView {
    protected val mLifecycleProvider = AndroidLifecycle.createLifecycleProvider(this)
    protected var progressDialog: ProgressDialog? = null
    protected var titleBar: CustomToolbar? = null
    protected var mToast: Toast? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { iniBundle(it) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = setCusContentView(inflater, container)
        titleBar = view.findViewById(R.id.titleBar)
        if (titleBar != null) {
            titleBar!!.setNavigationOnClickListener { navigationOnClickListener() }
        }
        return view
    }

    abstract fun iniBundle(bundle: Bundle)
    open fun setStatusBar() {}
    open fun setCusContentView(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): View {
        return LayoutInflater.from(context).inflate(getLayoutId(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setStatusBar()
        iniView(view)
        iniListener()
        iniData()
    }

    abstract fun getLayoutId(): Int
    abstract fun iniView(view: View)
    abstract fun iniListener()
    abstract fun iniData()
    fun setTitle(@StringRes resid: Int) {
        titleBar?.setTitle(resid)
    }

    fun setTitle(text: CharSequence?) {
        titleBar?.title = text
    }

    @UiThread
    override fun showProgressBar() {
        showProgressBar(true)
    }

    @UiThread
    override fun showProgressBar(msg: String) {
        showProgressBar(true, msg)
    }

    @UiThread
    override fun showProgressBar(@StringRes msg: Int) {
        if (!lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
            return
        }

        val message = getString(msg)
        showProgressBar(message)
    }

    @UiThread
    override fun showProgressBar(canCancel: Boolean) {
        showProgressBar(canCancel, R.string.progress_waiting)
    }

    @UiThread
    override fun showProgressBar(canCancel: Boolean, @StringRes msg: Int) {
        if (!lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
            return
        }

        val message = getString(msg)
        showProgressBar(canCancel, message)
    }

    @UiThread
    override fun showProgressBar(canCancel: Boolean, msg: String) {
        if (!lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
            return
        }

        if (progressDialog == null) {
            progressDialog = ProgressDialog(context)
        }
        if (progressDialog!!.isShowing) {
            return
        }
        progressDialog!!.setMessage(msg)
        progressDialog!!.setCancelable(canCancel)
        progressDialog!!.setCanceledOnTouchOutside(canCancel)
        try {
            progressDialog!!.show()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    @UiThread
    override fun closeProgressBar() {
        if (progressDialog != null && progressDialog!!.isShowing) {
            try {
                progressDialog!!.dismiss()
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

    @UiThread
    override fun showToastMessage(resId: Int) {
        showToastMessage(resId, Toast.LENGTH_SHORT)
    }

    @UiThread
    override fun showToastMessage(text: CharSequence) {
        showToastMessage(text, Toast.LENGTH_SHORT)
    }

    @UiThread
    override fun showToastMessage(@StringRes resId: Int, duration: Int) {
        if (!lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
            return
        }

        mToast?.cancel()
        mToast = Toast.makeText(requireContext(), resId, duration)
        mToast!!.show()
    }

    @UiThread
    override fun showToastMessage(text: CharSequence, duration: Int) {
        if (!lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
            return
        }

        mToast?.cancel()
        mToast = Toast.makeText(requireContext(), text, duration)
        mToast!!.show()
    }

    @MainThread
    open fun navigationOnClickListener() {
        (activity as BaseActivity?)!!.thisFinish()
    }

    @MainThread
    override fun thisFinish() {
        if (activity is BaseActivity) {
            (activity as BaseActivity).thisFinish()
        }
    }

    override fun onDestroy() {
        mToast = null
        closeProgressBar()
        super.onDestroy()
    }

    @Deprecated
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (this is EasyPermissions.PermissionCallbacks) {
            EasyPermissions
                .onRequestPermissionsResult(requestCode, permissions, grantResults, this)
        }
    }

    override fun getLifecycleOwner(): LifecycleOwner {
        return this
    }

    override fun getLifecycleProvider(): LifecycleProvider<Lifecycle.Event> {
        return mLifecycleProvider
    }
}