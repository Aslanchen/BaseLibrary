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
import androidx.annotation.CallSuper
import androidx.annotation.StringRes
import androidx.annotation.UiThread
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.aslan.baselibrary.R
import com.aslan.baselibrary.listener.IBaseView
import com.aslan.baselibrary.permissions.EasyPermissions
import com.aslan.baselibrary.permissions.EasyPermissions.PermissionCallbacks
import com.aslan.baselibrary.permissions.models.PermissionRequest
import com.aslan.baselibrary.widget.TopSnackbar
import com.trello.lifecycle2.android.lifecycle.AndroidLifecycle
import com.trello.rxlifecycle3.LifecycleProvider

/**
 * 基础类
 * 如果需要重写[WaitingDialog]，重写[initProgressDialog]方法、
 *
 * @author Aslan
 * @date 2018/4/11
 */
abstract class BaseDialogFragment : DialogFragment(), IBaseView {
    protected val provider = AndroidLifecycle.createLifecycleProvider(this)
    protected var progressDialog: WaitingDialog? = null
    protected var mToast: Toast? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { iniBundle(it) }

        EasyPermissions.add(this)
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

    @CallSuper
    override fun onDestroy() {
        mToast = null
        closeProgressBar()
        EasyPermissions.remove(this)
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

    /**
     * 应用市场审核需要，在申请权限之前，需要弹框给出提示，双屏显示
     */
    open fun showToastBeforeRequestPermission(request: PermissionRequest): TopSnackbar {
        val viewGroup = requireActivity().findViewById<ViewGroup>(android.R.id.content)
        val mTopSnackbar = TopSnackbar.make(viewGroup, request.title ?: "", request.rationale ?: "")
        mTopSnackbar.show()
        return mTopSnackbar
    }

    /**
     * 应用市场审核需要，在申请权限之前，需要弹框给出提示
     */
    open fun showDialogBeforeRequestPermission(request: PermissionRequest, agree: () -> Unit, refuse: () -> Unit) {
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

    open fun checkAndRequestPermission(tipType: EasyPermissions.TipType, request: PermissionRequest, callback: PermissionCallbacks) {
        if (tipType == EasyPermissions.TipType.Toast) {
            if (!EasyPermissions.hasPermissions(requireContext(), *request.perms)) {
                val mTopSnackbar = showToastBeforeRequestPermission(request)
                val callback2 = object : PermissionCallbacks {
                    override fun onPermissionsGranted(allGranted: Boolean, perms: List<String>) {
                        mTopSnackbar.dismiss()
                        callback.onPermissionsGranted(allGranted, perms)
                    }

                    override fun onPermissionsDenied(doNotAskAgain: Boolean, perms: List<String>) {
                        callback.onPermissionsDenied(doNotAskAgain, perms)

                        if (EasyPermissions.somePermissionPermanentlyDenied(this@BaseDialogFragment, perms)) {
                            //永久拒绝，只能跳转到设置界面
                            showPermissionPermanentlyDeniedDialog(mTopSnackbar)
                        } else {
                            mTopSnackbar.dismiss()
                        }
                    }
                }
                EasyPermissions.requestPermissions(this, request, callback2)
            } else {
                EasyPermissions.requestPermissions(this, request, callback)
            }
        } else if (tipType == EasyPermissions.TipType.Dialog) {
            if (!EasyPermissions.hasPermissions(requireContext(), *request.perms)) {
                showDialogBeforeRequestPermission(
                    request,
                    { EasyPermissions.requestPermissions(this, request, callback) },
                    { callback.onPermissionsDenied(false, request.perms.toList()) })
            } else {
                EasyPermissions.requestPermissions(this, request, callback)
            }
        }
    }

    private val launcherExternalStorageManager = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        onExternalStorageManagerResult(result)
    }

    open fun onExternalStorageManagerResult(result: ActivityResult) {

    }

    /**
     * 需要全面访问外部存储（例如文件管理器应用）
     */
    open fun checkAndRequestExternalStorageManager(request: PermissionRequest, refuse: () -> Unit): Boolean {
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

    private var mRequestSDPermission: PermissionRequest? = null//SD权限

    fun getRequestSDPermission(): PermissionRequest {
        if (mRequestSDPermission == null) {
            mRequestSDPermission = PermissionRequest.Builder(requireContext())
                .perms(EasyPermissions.PERMISSIONS_EXTERNAL_STORAGE)
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
    open fun checkAndRequestSDPermission(tipType: EasyPermissions.TipType, callback: PermissionCallbacks) {
        val request = getRequestSDPermission()
        checkAndRequestPermission(tipType, request, callback)
    }

    private val launcherApplicationDetailSettings = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        onPermissionManualSettingResult(result)
    }

    /**
     * 权限被永久拒绝，需要去设置界面，手动设置
     */
    fun showPermissionPermanentlyDeniedDialog(mTopSnackbar: TopSnackbar) {
        AlertDialog.Builder(requireContext())
            .setCancelable(false)
            .setTitle(R.string.permissions)
            .setMessage(R.string.request_permission_permanently_denied)
            .setPositiveButton(R.string.go_setting) { dialog, which ->
                mTopSnackbar.dismiss()

                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", requireContext().packageName, null)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                launcherApplicationDetailSettings.launch(intent)
            }
            .setNegativeButton(R.string.refuse) { dialog, which ->
                mTopSnackbar.dismiss()
            }
            .show()
    }

    /**
     * 权限手动设置后回调
     */
    open fun onPermissionManualSettingResult(result: ActivityResult) {

    }
}