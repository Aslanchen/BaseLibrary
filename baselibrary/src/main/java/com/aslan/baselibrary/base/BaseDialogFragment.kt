package com.aslan.baselibrary.base

import android.app.ProgressDialog
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
import com.aslan.baselibrary.R
import com.aslan.baselibrary.listener.IBaseView
import com.trello.lifecycle2.android.lifecycle.AndroidLifecycle
import com.trello.rxlifecycle3.LifecycleProvider
import pub.devrel.easypermissions.EasyPermissions
import pub.devrel.easypermissions.EasyPermissions.PermissionCallbacks

/**
 * 基础类
 *
 * @author Aslan
 * @date 2018/4/11
 */
abstract class BaseDialogFragment : DialogFragment(), IBaseView {
    protected val provider = AndroidLifecycle.createLifecycleProvider(this)
    protected var progressDialog: ProgressDialog? = null
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
        if (isAdd == false) {
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
        if (isAdd == false) {
            return
        }
        val message = getString(msg)
        showProgressBar(canCancel, message)
    }

    @UiThread
    override fun showProgressBar(canCancel: Boolean, msg: String) {
        if (isAdd == false) {
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
    override fun showToastMessage(@StringRes resId: Int) {
        if (isAdd == false) {
            return
        }
        Toast.makeText(context, resId, Toast.LENGTH_SHORT).show()
    }

    @UiThread
    override fun showToastMessage(text: CharSequence) {
        if (isAdd == false) {
            return
        }
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

    override fun isAdd(): Boolean {
        return this.isAdded
    }

    override fun onDestroy() {
        closeProgressBar()
        super.onDestroy()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (this is PermissionCallbacks) {
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