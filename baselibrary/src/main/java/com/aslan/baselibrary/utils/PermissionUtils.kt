package com.aslan.baselibrary.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.os.Build
import androidx.annotation.Size
import androidx.annotation.StringRes
import androidx.annotation.StyleRes
import androidx.fragment.app.Fragment
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog

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

    val PERMISSIONS_VIDEO_AND_ALBUM = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        arrayOf(
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_AUDIO,
            Manifest.permission.READ_MEDIA_VIDEO,
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
        )
    } else {
        arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
        )
    }

    /**
     * 释放具备权限
     */
    fun hasPermissions(host: Context, @Size(min = 1) vararg perms: String): Boolean {
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
        EasyPermissions.requestPermissions(
            host,
            com.vmadalin.easypermissions.models.PermissionRequest(
                request.theme,
                request.code,
                request.perms,
                request.rationale,
                request.positiveButtonText,
                request.negativeButtonText,
            )
        )
    }

    /**
     * 请求权限
     */
    fun requestPermissions(host: Activity, request: PermissionRequest) {
        EasyPermissions.requestPermissions(
            host,
            com.vmadalin.easypermissions.models.PermissionRequest(
                request.theme,
                request.code,
                request.perms,
                request.rationale,
                request.positiveButtonText,
                request.negativeButtonText,
            )
        )
    }

    /**
     * 检查并且请求
     */
    fun checkAndRequestPermissions(host: Fragment, request: PermissionRequest, @Size(min = 1) vararg perms: String): Boolean {
        if (!EasyPermissions.hasPermissions(host.context, *perms)) {
            EasyPermissions.requestPermissions(
                host,
                com.vmadalin.easypermissions.models.PermissionRequest(
                    request.theme,
                    request.code,
                    request.perms,
                    request.rationale,
                    request.positiveButtonText,
                    request.negativeButtonText,
                )
            )
            return false
        }

        return true
    }

    /**
     * 检查并且请求
     */
    fun checkAndRequestPermissions(host: Activity, request: PermissionRequest, @Size(min = 1) vararg perms: String): Boolean {
        if (!EasyPermissions.hasPermissions(host, *perms)) {
            EasyPermissions.requestPermissions(
                host,
                com.vmadalin.easypermissions.models.PermissionRequest(
                    request.theme,
                    request.code,
                    request.perms,
                    request.rationale,
                    request.positiveButtonText,
                    request.negativeButtonText,
                )
            )
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
     * 被拒绝，这意味着请求的某些权限被用户拒绝了，但这不是永久性拒绝，用户可以在设置中重新打开这些权限
     */
    fun somePermissionDenied(host: Fragment, @Size(min = 1) vararg perms: String): Boolean {
        return EasyPermissions.somePermissionDenied(host, *perms)
    }

    /**
     * 被拒绝，这意味着请求的某些权限被用户拒绝了，但这不是永久性拒绝，用户可以在设置中重新打开这些权限
     */
    fun somePermissionDenied(host: Activity, @Size(min = 1) vararg perms: String): Boolean {
        return EasyPermissions.somePermissionDenied(host, *perms)
    }

    /**
     * 被拒绝，这表示用户在过去拒绝了这些权限，并选择了“不再询问”的选项，这是永久性拒绝，因此不能仅仅通过权限请求弹窗来解决问题，需要用户手动在设置中修改应用的权限。
     */
    fun somePermissionPermanentlyDenied(host: Fragment, @Size(min = 1) perms: List<String>): Boolean {
        return EasyPermissions.somePermissionPermanentlyDenied(host, perms)
    }

    /**
     * 被拒绝，这表示用户在过去拒绝了这些权限，并选择了“不再询问”的选项，这是永久性拒绝，因此不能仅仅通过权限请求弹窗来解决问题，需要用户手动在设置中修改应用的权限。
     */
    fun somePermissionPermanentlyDenied(host: Activity, @Size(min = 1) perms: List<String>): Boolean {
        return EasyPermissions.somePermissionPermanentlyDenied(host, perms)
    }

    data class PermissionRequest(
        @StyleRes
        var theme: Int,
        var code: Int,
        var perms: Array<out String>,
        var title: String?,
        var rationale: String?,
        var positiveButtonText: String?,
        var negativeButtonText: String?
    ) {

        @Suppress("UNUSED")
        class Builder(var context: Context?) {
            @StyleRes
            private var theme = 0
            private var code = 0
            private var perms: Array<out String> = emptyArray()
            private var title: String? = null
            private var rationale: String? = null
            private var positiveButtonText = context?.getString(android.R.string.ok)
            private var negativeButtonText = context?.getString(android.R.string.cancel)

            fun theme(@StyleRes theme: Int) = apply { this.theme = theme }
            fun code(code: Int) = apply { this.code = code }
            fun perms(perms: Array<out String>) = apply { this.perms = perms }
            fun title(rationale: String) = apply { this.title = rationale }
            fun title(@StringRes resId: Int) = apply { this.title = context?.getString(resId) }
            fun rationale(rationale: String) = apply { this.rationale = rationale }
            fun rationale(@StringRes resId: Int) = apply { this.rationale = context?.getString(resId) }
            fun positiveButtonText(positiveButtonText: String) =
                apply { this.positiveButtonText = positiveButtonText }

            fun positiveButtonText(@StringRes resId: Int) =
                apply { this.positiveButtonText = context?.getString(resId) }

            fun negativeButtonText(negativeButtonText: String) =
                apply { this.negativeButtonText = negativeButtonText }

            fun negativeButtonText(@StringRes resId: Int) =
                apply { this.negativeButtonText = context?.getString(resId) }

            fun build(): PermissionRequest {
                return PermissionRequest(
                    theme = theme,
                    code = code,
                    perms = perms,
                    title = title,
                    rationale = rationale,
                    positiveButtonText = positiveButtonText,
                    negativeButtonText = negativeButtonText
                )
            }
        }
    }
}
