package com.aslan.baselibrary.base

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.MainThread
import androidx.annotation.StringRes
import androidx.annotation.UiThread
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.aslan.baselibrary.R
import com.aslan.baselibrary.listener.IBaseView
import com.aslan.baselibrary.view.CustomToolbar
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
abstract class BaseActivity : AppCompatActivity(), IBaseView {
    protected val provider = AndroidLifecycle.createLifecycleProvider(this)
    protected var progressDialog: ProgressDialog? = null
    protected var titleBar: CustomToolbar? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intent.extras?.let { iniBundle(it) }
        setCusContentView()
        titleBar = findViewById(R.id.titleBar)
        if (titleBar != null) {
            titleBar!!.setNavigationOnClickListener { navigationOnClickListener() }
        }
        iniView()
        iniListener()
        iniData()
    }

    open fun setCusContentView() {
        setContentView(getLayoutId())
    }

    abstract fun iniBundle(bundle: Bundle)
    abstract fun getLayoutId(): Int
    abstract fun iniView()
    abstract fun iniListener()
    abstract fun iniData()
    override fun setTitle(title: CharSequence) {
        titleBar?.title = title
    }

    override fun setTitle(@StringRes titleId: Int) {
        titleBar?.setTitle(titleId)
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
            progressDialog = ProgressDialog(this)
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
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show()
    }

    @UiThread
    override fun showToastMessage(text: CharSequence) {
        if (isAdd == false) {
            return
        }
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    fun navigationOnClickListener() {
        thisFinish()
    }

    @MainThread
    override fun thisFinish() {
        finish()
    }

    override fun isAdd(): Boolean {
        return !isFinishing
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

    override fun requireContext(): Context {
        return this
    }

    override fun getLifecycleOwner(): LifecycleOwner {
        return this
    }

    override fun getLifecycleProvider(): LifecycleProvider<Lifecycle.Event> {
        return provider
    }
}