package com.aslan.baselibrary.base

import android.app.DownloadManager
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.MainThread
import androidx.annotation.StringRes
import androidx.annotation.UiThread
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.aslan.baselibrary.R
import com.aslan.baselibrary.event.EventDownload
import com.aslan.baselibrary.listener.IBaseView
import com.aslan.baselibrary.utils.AppUtil
import com.aslan.baselibrary.utils.FileUtil
import com.aslan.baselibrary.utils.LogUtils
import com.aslan.baselibrary.utils.PermissionUtils
import com.aslan.baselibrary.view.CustomToolbar
import com.jaeger.library.StatusBarUtil
import com.trello.lifecycle2.android.lifecycle.AndroidLifecycle
import com.trello.rxlifecycle3.LifecycleProvider
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.models.PermissionRequest
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.File

/**
 * 基础类
 * 如果需要重写[WaitingDialog]，重写[initProgressDialog]方法
 *
 * @author Aslan
 * @date 2018/4/11
 */
abstract class BaseActivity : AppCompatActivity(), IBaseView {
    companion object {
        const val REQUEST_CODE_SD_PERMISSION = 6150
    }

    protected val mLifecycleProvider = AndroidLifecycle.createLifecycleProvider(this)
    protected var progressDialog: WaitingDialog? = null
    protected var isProgressDialogShowing = false
    protected var titleBar: CustomToolbar? = null
    protected var mToast: Toast? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intent.extras?.let { iniBundle(it) }
        setCusContentView()
        setStatusBar()

        titleBar = findViewById(R.id.titleBar)
        if (titleBar != null) {
            titleBar!!.setNavigationOnClickListener { navigationOnClickListener() }
        }
        iniView()
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
        iniListener()
        iniData()
    }

    open fun setStatusBar() {
        StatusBarUtil.setDarkMode(this)
        StatusBarUtil.setTranslucent(this, 0)
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
                progressDialog!!.show(supportFragmentManager, msg, true)
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
            mToast = Toast.makeText(this@BaseActivity, resId, duration)
            mToast!!.show()
        }
    }

    @UiThread
    override fun showToastMessage(text: CharSequence, duration: Int) {
        lifecycleScope.launchWhenResumed {
            mToast?.cancel()
            mToast = Toast.makeText(this@BaseActivity, text, duration)
            mToast!!.show()
        }
    }

    @MainThread
    open fun navigationOnClickListener() {
        thisFinish()
    }

    @MainThread
    override fun thisFinish() {
        finish()
    }

    override fun onDestroy() {
        mToast = null
        closeProgressBar()

        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
        super.onDestroy()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (this is EasyPermissions.PermissionCallbacks) {
            EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
        }
    }

    override fun requireContext(): Context {
        return this
    }

    override fun getLifecycleOwner(): LifecycleOwner {
        return this
    }

    override fun getLifecycleProvider(): LifecycleProvider<Lifecycle.Event> {
        return mLifecycleProvider
    }

    private val launcherExternalStorageManager = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        onExternalStorageManager(result)
    }

    open fun onExternalStorageManager(result: ActivityResult) {
        if (!PermissionUtils.hasPermissions(this, *PermissionUtils.PERMISSIONS_EXTERNAL_STORAGE)) {
            PermissionUtils.requestPermissions(
                this@BaseActivity,
                PermissionRequest.Builder(this@BaseActivity)
                    .code(REQUEST_CODE_SD_PERMISSION)
                    .perms(PermissionUtils.PERMISSIONS_EXTERNAL_STORAGE)
                    .rationale(R.string.request_permission_down)
                    .positiveButtonText(R.string.agree)
                    .negativeButtonText(R.string.refuse)
                    .build()
            )
        }
    }

    /**
     * SD卡申请选线
     */
    open fun showDialogBeforeRequestSDPermission(agree: () -> Unit, refuse: () -> Unit) {
        showDialogBeforeRequestPermission(
            AlertDialog.Builder(this)
                .setTitle(R.string.permissions)
                .setMessage(getString(R.string.request_permission_down)), agree, refuse
        )
    }

    fun showDialogBeforeRequestPermission(build: AlertDialog.Builder, agree: () -> Unit, refuse: () -> Unit) {
        showDialogBeforeRequestPermission(build, R.string.agree, R.string.refuse, agree, refuse)
    }

    /**
     * 应用市场审核需要，在申请权限之前，需要弹框给出提示
     */
    fun showDialogBeforeRequestPermission(
        build: AlertDialog.Builder, @StringRes agreeTextId: Int, @StringRes refuseTextId: Int,
        agree: () -> Unit, refuse: () -> Unit
    ) {
        build
            .setPositiveButton(agreeTextId) { dialog, which ->
                agree()
            }
            .setNegativeButton(refuseTextId) { dialog, which ->
                refuse()
            }
            .show()
    }

    /**
     * 应用市场审核需要，在申请权限之前，需要弹框给出提示
     */
    fun showDialogBeforeRequestPermission(
        build: AlertDialog.Builder, agreeText: String, refuseText: String,
        agree: () -> Unit, refuse: () -> Unit
    ) {
        build
            .setPositiveButton(agreeText) { dialog, which ->
                agree()
            }
            .setNegativeButton(refuseText) { dialog, which ->
                refuse()
            }
            .show()
    }

    /**
     * 检查并且请求权限
     */
    open fun checkAndRequestPermission(build: AlertDialog.Builder, vararg perms: String, request: PermissionRequest): Boolean {
        if (!PermissionUtils.hasPermissions(this, *perms)) {
            if (PermissionUtils.somePermissionDenied(this, *perms)) {
                PermissionUtils.newAppSettingsDialogBuilder(this)
                    .requestCode(request.code)
                    .rationale(request.rationale!!)
                    .negativeButtonText(request.negativeButtonText!!)
                    .positiveButtonText(request.positiveButtonText!!)
                    .openOnNewTask(true)
                    .build()
                    .show()
            } else {
                showDialogBeforeRequestPermission(build, { PermissionUtils.requestPermissions(this@BaseActivity, request) }, {})
            }
            return false
        }

        return true
    }

    /**
     * 检查并且请求SD卡读写权限
     */
    open fun checkAndRequestSDPermission(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                showDialogBeforeRequestSDPermission({
                    val packageUri = Uri.parse("package:$packageName")
                    val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, packageUri)
                    launcherExternalStorageManager.launch(intent)
                }, {})
                return false
            }
        }

        if (!PermissionUtils.hasPermissions(this, *PermissionUtils.PERMISSIONS_EXTERNAL_STORAGE)) {
            if (PermissionUtils.somePermissionDenied(this, *PermissionUtils.PERMISSIONS_EXTERNAL_STORAGE)) {
                PermissionUtils.newAppSettingsDialogBuilder(this)
                    .requestCode(REQUEST_CODE_SD_PERMISSION)
                    .rationale(R.string.request_permission_down)
                    .negativeButtonText(R.string.agree)
                    .positiveButtonText(R.string.refuse)
                    .openOnNewTask(true)
                    .build()
                    .show()
            } else {
                showDialogBeforeRequestSDPermission({
                    PermissionUtils.requestPermissions(
                        this@BaseActivity,
                        PermissionRequest.Builder(this@BaseActivity)
                            .code(REQUEST_CODE_SD_PERMISSION)
                            .perms(PermissionUtils.PERMISSIONS_EXTERNAL_STORAGE)
                            .rationale(R.string.request_permission_down)
                            .positiveButtonText(R.string.agree)
                            .negativeButtonText(R.string.refuse)
                            .build()
                    )
                }, {})
            }
            return false
        }

        return true
    }

    private val launcherInstall = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        onManageUnknownAppSourcesCallback(result)
    }

    protected fun showPackageInstallDialog(agree: () -> Unit, refuse: () -> Unit) {
        AlertDialog.Builder(requireContext())
            .setMessage(getString(R.string.request_permission_install_apk))
            .setPositiveButton(R.string.agree) { dialog, which ->
                agree()
            }
            .setNegativeButton(R.string.refuse) { dialog, which ->
                refuse()
            }
            .show()
    }

    /**
     * 请求安装未知应用权限
     */
    open fun requestPackageInstalls(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val packageInfo = AppUtil.getPackageInfo(requireContext()) ?: return true

            //Android8.0开始需要获取应用内安装权限
            val allowInstall = packageManager.canRequestPackageInstalls()
            //如果还没有授权安装应用，去设置内开启应用内安装权限
            if (!allowInstall) {
                //注意这个是8.0新API
                showPackageInstallDialog({
                    val packageUri = Uri.parse("package:" + packageInfo.packageName)
                    val intentX = Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageUri)
                    launcherInstall.launch(intentX)
                }, {})
                return false
            }
        }

        return true
    }

    protected open fun onManageUnknownAppSourcesCallback(result: ActivityResult) {

    }

    protected fun downloadByManager(url: String, filename: String, title: String, description: String, downloadId: Long? = -1): Long {
        if (downloadId != null && downloadId != -1L) {
            val status = getStatusForDownloadedFile(downloadId)
            if (status == DownloadManager.STATUS_RUNNING) {
                showToastMessage(R.string.download_is_in_download)
                return -1
            }
        }

        val root = FileUtil.getDownload(this)
        val file = File(root, filename)
        LogUtils.d(file.path)
        if (file.exists()) {
            file.delete()
        }

        val mDownloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        val request = DownloadManager.Request(Uri.parse(url))
            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED).setTitle(title).setDescription(description)
            .setDestinationInExternalFilesDir(this, Environment.DIRECTORY_DOWNLOADS, file.name)
        return mDownloadManager.enqueue(request)
    }

    private fun getStatusForDownloadedFile(id: Long): Int? {
        val mDownloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        val query = DownloadManager.Query().setFilterById(id)
        var cursor: Cursor? = null
        try {
            cursor = mDownloadManager.query(query)
            if (cursor == null) {
                return null
            }

            if (cursor.moveToFirst()) {
                return cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS))
            }
        } finally {
            cursor?.close()
        }
        return null
    }

    protected fun downloadByBrowser(url: String) {
        val uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setData(uri)
        intent.addCategory(Intent.CATEGORY_BROWSABLE)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            startActivity(Intent.createChooser(intent, "请选择浏览器"))
        } catch (ex: ActivityNotFoundException) {
            showToastMessage("没有匹配的程序")
        }
    }

    protected open fun onFileDownload(event: EventDownload) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEventDownload(event: EventDownload) {
        onFileDownload(event)
    }
}