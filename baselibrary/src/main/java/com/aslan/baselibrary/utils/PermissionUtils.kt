package com.aslan.baselibrary.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.os.Build
import androidx.annotation.Size
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import com.vmadalin.easypermissions.models.PermissionRequest

/**
 * 权限管理模块
 *
 * @AfterPermissionGranted
 * 使用了该注解，当权限请求被用户同意后，会根据请求code来执行，相应的含有@AfterPermissionGranted注解的方法
 * @AfterPermissionGranted([requestcode])
 * fun callback(){}
 *
 */
object PermissionUtils {
    init {
        throw UnsupportedOperationException("Do not need instantiate!")
    }

    val PERMISSIONS_EXTERNAL_STORAGE = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        arrayOf(
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_AUDIO,
            Manifest.permission.READ_MEDIA_VIDEO,
        )
    } else {
        arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
        )
    }

    val PERMISSIONS_CAMERA_AND_ALBUM = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        arrayOf(
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_AUDIO,
            Manifest.permission.READ_MEDIA_VIDEO,
            Manifest.permission.CAMERA,
        )
    } else {
        arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
        )
    }

    /**
     * 释放具备权限
     */
    fun hasPermissions(host: Context, vararg perms: String): Boolean {
        return EasyPermissions.hasPermissions(host, *perms)
    }

    /**
     * 请求权限
     */
    fun requestPermissions(host: Fragment, @StringRes rationaleResId: Int, requestCode: Int, @Size(min = 1) vararg perms: String) {
        requestPermissions(host, host.getString(rationaleResId), requestCode, *perms)
    }

    /**
     * 请求权限
     */
    fun requestPermissions(host: Activity, @StringRes rationaleResId: Int, requestCode: Int, @Size(min = 1) vararg perms: String) {
        requestPermissions(host, host.getString(rationaleResId), requestCode, *perms)
    }

    /**
     * 请求权限
     */
    fun requestPermissions(host: Fragment, rationale: String, requestCode: Int, @Size(min = 1) vararg perms: String) {
        EasyPermissions.requestPermissions(host, rationale, requestCode, *perms)
    }

    /**
     * 请求权限
     */
    fun requestPermissions(host: Activity, rationale: String, requestCode: Int, @Size(min = 1) vararg perms: String) {
        EasyPermissions.requestPermissions(host, rationale, requestCode, *perms)
    }

    /**
     * 请求权限
     */
    fun requestPermissions(host: Fragment, request: PermissionRequest) {
        EasyPermissions.requestPermissions(host, request)
    }

    /**
     * 请求权限
     */
    fun requestPermissions(host: Activity, request: PermissionRequest) {
        EasyPermissions.requestPermissions(host, request)
    }

    /**
     * 检查并且请求
     */
    fun checkAndRequestPermissions(host: Fragment, vararg perms: String, request: PermissionRequest): Boolean {
        if (!EasyPermissions.hasPermissions(host.context, *perms)) {
            EasyPermissions.requestPermissions(host, request)
            return false
        }

        return true
    }

    /**
     * 检查并且请求
     */
    fun checkAndRequestPermissions(host: Activity, vararg perms: String, request: PermissionRequest): Boolean {
        if (!EasyPermissions.hasPermissions(host, *perms)) {
            EasyPermissions.requestPermissions(host, request)
            return false
        }

        return true
    }

    fun newPermissionRequestBuilder(host: Context): PermissionRequest.Builder {
        return PermissionRequest.Builder(host)
    }

    fun newPermissionRequestBuilder(host: Context, requestCode: Int, @Size(min = 1) vararg perms: String): PermissionRequest.Builder {
        return PermissionRequest.Builder(host)
            .code(requestCode)
            .perms(perms)
    }

    /**
     * 进入设置界面
     */
    fun newAppSettingsDialogBuilder(host: Context): SettingsDialog.Builder {
        return SettingsDialog.Builder(host)
    }

    /**
     * 被拒绝
     */
    fun somePermissionDenied(host: Fragment, vararg perms: String): Boolean {
        return EasyPermissions.somePermissionDenied(host, *perms)
    }

    /**
     * 被拒绝
     */
    fun somePermissionDenied(host: Activity, vararg perms: String): Boolean {
        return EasyPermissions.somePermissionDenied(host, *perms)
    }
}
