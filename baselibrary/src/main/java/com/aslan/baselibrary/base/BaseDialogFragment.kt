package com.aslan.baselibrary.base

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.annotation.UiThread
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.aslan.baselibrary.R
import com.aslan.baselibrary.base.BaseActivity.Companion.REQUEST_CODE_SD_PERMISSION
import com.aslan.baselibrary.base.BaseActivity.Companion.REQUEST_CODE_SETTING_PERMANENTLY_DENIED
import com.aslan.baselibrary.listener.IBaseView
import com.aslan.baselibrary.utils.PermissionUtils
import com.trello.lifecycle2.android.lifecycle.AndroidLifecycle
import com.trello.rxlifecycle3.LifecycleProvider
import com.vmadalin.easypermissions.EasyPermissions
import java.lang.Deprecated

/**
 * 基础类
 * 如果需要重写[WaitingDialog]，重写[initProgressDialog]方法、
 *
 * @author Aslan
 * @date 2018/4/11
 */
abstract class BaseDialogFragment : DialogFragment(), IBaseView, EasyPermissions.PermissionCallbacks {
    protected val provider = AndroidLifecycle.createLifecycleProvider(this)
    protected var progressDialog: WaitingDialog? = null
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
                progressDialog = WaitingDialog.Builder(requireContext())
                    .setCancelable(canCancel)
                    .setMessage(msg)
                    .show(parentFragmentManager)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
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

    override fun thisFinish() {
        dismiss()
    }

    override fun getLifecycleOwner(): LifecycleOwner {
        return this
    }

    override fun getLifecycleProvider(): LifecycleProvider<Lifecycle.Event> {
        return provider
    }

    @Deprecated
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    protected var requestPermissionLast: PermissionUtils.PermissionRequest? = null

    /**
     * 检查并且请求权限
     *
     * 应用市场审核需要，在申请权限之前，需要弹框给出提示
     *
     */
    open fun checkAndRequestPermission(request: PermissionUtils.PermissionRequest, agree: () -> Unit, refuse: () -> Unit): Boolean {
        if (!PermissionUtils.hasPermissions(requireContext(), *request.perms)) {
            requestPermissionLast = request
            showDialogBeforeRequestPermission(request, agree, refuse)
            return false
        }

        return true
    }

    open fun checkAndRequestPermission(request: PermissionUtils.PermissionRequest): Boolean {
        if (!PermissionUtils.hasPermissions(requireContext(), *request.perms)) {
            requestPermissionLast = request
            showDialogBeforeRequestPermission(request, { PermissionUtils.requestPermissions(this, request) }, {})
            return false
        }

        return true
    }

    private val launcherExternalStorageManager = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        onExternalStorageManagerResult(result)
    }

    open fun onExternalStorageManagerResult(result: ActivityResult) {

    }

    /**
     * 需要全面访问外部存储（例如文件管理器应用）
     */
    open fun checkAndRequestExternalStorageManager(request: PermissionUtils.PermissionRequest, refuse: () -> Unit): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // Android11以上需要申请所有文件访问权限
            if (!Environment.isExternalStorageManager()) {
                showDialogBeforeRequestPermission(request, {
                    val packageUri = Uri.parse("package:${requireContext().packageName}")
                    val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, packageUri)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    launcherExternalStorageManager.launch(intent)
                }, refuse)
                return false
            }
        }

        return true
    }

    private var mRequestSDPermission: PermissionUtils.PermissionRequest? = null//SD权限

    fun getRequestSDPermission(): PermissionUtils.PermissionRequest {
        if (mRequestSDPermission == null) {
            mRequestSDPermission = PermissionUtils.PermissionRequest.Builder(requireContext())
                .code(REQUEST_CODE_SD_PERMISSION)
                .perms(PermissionUtils.PERMISSIONS_EXTERNAL_STORAGE)
                .title(R.string.permissions)
                .rationale(R.string.request_permission_down)
                .positiveButtonText(R.string.agree)
                .negativeButtonText(R.string.refuse)
                .build()
        }
        return mRequestSDPermission!!
    }

    /**
     * 检查并且请求SD卡读写权限
     */
    open fun checkAndRequestSDPermission(refuse: () -> Unit): Boolean {
        val request = getRequestSDPermission()
        return checkAndRequestPermission(request, { PermissionUtils.requestPermissions(this, request) }, refuse)
    }

    /**
     * 应用市场审核需要，在申请权限之前，需要弹框给出提示
     */
    open fun showDialogBeforeRequestPermission(request: PermissionUtils.PermissionRequest, agree: () -> Unit, refuse: () -> Unit) {
        AlertDialog.Builder(requireContext())
            .setTitle(request.title)
            .setMessage(request.rationale)
            .setPositiveButton(request.positiveButtonText) { dialog, which ->
                agree()
            }
            .setNegativeButton(request.negativeButtonText) { dialog, which ->
                refuse()
            }
            .show()
    }

    /**
     * 权限被授予
     */
    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        if (requestPermissionLast != null) {
            val request = requestPermissionLast!!
            if (request.code != requestCode) {
                return
            }

            if (PermissionUtils.somePermissionPermanentlyDenied(this, requestPermissionLast!!.perms.toList()).not()) {
                requestPermissionLast = null
            }
        }
    }

    /**
     * 权限被拒绝
     */
    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if (requestPermissionLast != null) {
            val request = requestPermissionLast!!
            if (request.code != requestCode) {
                return
            }

            if (PermissionUtils.somePermissionPermanentlyDenied(this, perms)) {
                //永久拒绝，只能跳转到设置界面
                PermissionUtils.newAppSettingsDialogBuilder(requireContext())
                    .title(R.string.permissions)
                    .requestCode(REQUEST_CODE_SETTING_PERMANENTLY_DENIED)
                    .rationale(R.string.request_permission_permanently_denied)
                    .negativeButtonText(R.string.refuse)
                    .positiveButtonText(R.string.go_setting)
                    .openOnNewTask(true)
                    .build()
                    .show()
            }
            requestPermissionLast = null
        }
    }
}