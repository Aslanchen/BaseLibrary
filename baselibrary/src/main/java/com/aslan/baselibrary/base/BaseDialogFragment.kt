package com.aslan.baselibrary.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.annotation.UiThread
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.aslan.baselibrary.R
import com.aslan.baselibrary.listener.IBaseView
import com.trello.lifecycle2.android.lifecycle.AndroidLifecycle
import com.trello.rxlifecycle3.LifecycleProvider
import com.vmadalin.easypermissions.EasyPermissions

/**
 * 基础类
 *
 * @author Aslan
 * @date 2018/4/11
 */
abstract class BaseDialogFragment : DialogFragment(), IBaseView {
    protected val provider = AndroidLifecycle.createLifecycleProvider(this)
    protected var progressDialog: WaitingDialog? = null
    protected var isProgressDialogShowing = false
    protected var mToast: Toast? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { iniBundle(it) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(getLayoutId(), container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iniView(view)
        iniListener()
        iniData()
    }

    abstract fun iniBundle(bundle: Bundle)
    abstract fun getLayoutId(): Int
    abstract fun iniView(view: View)
    abstract fun iniListener()
    abstract fun iniData()

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
        val message = getString(msg)
        showProgressBar(message)
    }

    @UiThread
    override fun showProgressBar(canCancel: Boolean) {
        showProgressBar(canCancel, R.string.progress_waiting)
    }

    @UiThread
    override fun showProgressBar(canCancel: Boolean, @StringRes msg: Int) {
        val message = getString(msg)
        showProgressBar(canCancel, message)
    }

    @UiThread
    override fun showProgressBar(canCancel: Boolean, msg: String) {
        lifecycleScope.launchWhenResumed {
            if (progressDialog == null) {
                progressDialog = initProgressDialog()
            }

            if (isProgressDialogShowing) {
                return@launchWhenResumed
            }

            if (progressDialog!!.isAdded) {
                return@launchWhenResumed
            }

            if (progressDialog!!.isVisible) {
                return@launchWhenResumed
            }

            if (progressDialog!!.dialog?.isShowing == true) {
                return@launchWhenResumed
            }

            try {
                progressDialog!!.isCancelable = canCancel
                progressDialog!!.show(parentFragmentManager, msg, true)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
            isProgressDialogShowing = true
        }
    }

    @UiThread
    override fun closeProgressBar() {
        if (progressDialog != null) {
            try {
                progressDialog!!.dismiss()
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
            isProgressDialogShowing = false
        }
    }

    protected open fun initProgressDialog(): WaitingDialog {
        return WaitingDialog()
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
        lifecycleScope.launchWhenResumed {
            mToast?.cancel()
            mToast = Toast.makeText(requireContext(), resId, duration)
            mToast!!.show()
        }
    }

    @UiThread
    override fun showToastMessage(text: CharSequence, duration: Int) {
        lifecycleScope.launchWhenResumed {
            mToast?.cancel()
            mToast = Toast.makeText(requireContext(), text, duration)
            mToast!!.show()
        }
    }

    override fun onDestroy() {
        mToast = null
        closeProgressBar()
        super.onDestroy()
    }

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

    override fun thisFinish() {
        dismiss()
    }

    override fun getLifecycleOwner(): LifecycleOwner {
        return this
    }

    override fun getLifecycleProvider(): LifecycleProvider<Lifecycle.Event> {
        return provider
    }
}