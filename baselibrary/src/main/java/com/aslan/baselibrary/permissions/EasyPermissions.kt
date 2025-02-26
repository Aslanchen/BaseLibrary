/*
 * Copyright 2019 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.aslan.baselibrary.permissions

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.Size
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.aslan.baselibrary.permissions.helpers.base.PermissionsHelper
import com.aslan.baselibrary.permissions.models.PermissionRequest
import com.aslan.baselibrary.widget.TopSnackbar

private const val TAG = "EasyPermissions"

/**
 * Utility to request and check System permissions for apps targeting Android M (API &gt;= 23).
 */
@Suppress("UNUSED")
object EasyPermissions {
    var mTipType = EasyPermissions.TipType.Dialog
    var mIViewProvider: IViewProvider = DefaultViewProvider()

    enum class TipType {
        Dialog, Toast
    }

    //定位权限
    val PERMISSIONS_LOCATION = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
    )

    //外部拓展存储
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

    //拍照以及相册
    val PERMISSIONS_IMAGE_AND_ALBUM = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        arrayOf(
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.CAMERA,
        )
    } else {
        arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
        )
    }

    //录视频以及相册
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

    //录音以及文件
    val PERMISSIONS_AUDIO_AND_ALBUM = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        arrayOf(
            Manifest.permission.READ_MEDIA_AUDIO,
            Manifest.permission.RECORD_AUDIO,
        )
    } else {
        arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO,
        )
    }

    /**
     * Callback interface to receive the results of `EasyPermissions.requestPermissions()` calls.
     */
    interface PermissionCallbacks {

        fun onPermissionsGranted(allGranted: Boolean, perms: List<String>)

        fun onPermissionsDenied(doNotAskAgain: Boolean, perms: List<String>)

        fun onRationaleAccepted()

        fun onRationaleDenied()
    }

    abstract class SimplePermissionCallbacks : PermissionCallbacks {
        override fun onRationaleAccepted() {
        }

        override fun onRationaleDenied() {
        }
    }

    private val maps = mutableMapOf<Any, ActivityResultLauncher<Array<String>>>()

    @JvmStatic
    fun add(host: Any) {
        val callback =
            ActivityResultCallback<Map<String, @JvmSuppressWildcards Boolean>> { grantResults ->
                val grantedList = mutableListOf<String>()
                val deniedList = mutableListOf<String>()
                for ((permission, granted) in grantResults) {
                    if (granted) {
                        grantedList.add(permission)
                    } else {
                        deniedList.add(permission)
                    }
                }

                if (grantedList.isNotEmpty()) {
                    last?.callback?.onPermissionsGranted(deniedList.isEmpty(), grantedList)
                }

                if (deniedList.isNotEmpty()) {
                    val doNotAskAgain = if (host is AppCompatActivity) {
                        PermissionsHelper.newInstance(host).somePermissionPermanentlyDenied(deniedList)
                    } else if (host is Fragment) {
                        PermissionsHelper.newInstance(host).somePermissionPermanentlyDenied(deniedList)
                    } else {
                        false
                    }
                    last?.callback?.onPermissionsDenied(doNotAskAgain, deniedList)
                }
            }

        if (host is AppCompatActivity) {
            val mLauncher = host.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions(), callback)
            maps.put(host, mLauncher)
        } else if (host is Fragment) {
            val mLauncher = host.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions(), callback)
            maps.put(host, mLauncher)
        }
    }

    @JvmStatic
    fun remove(host: Any) {
        maps.remove(host)
    }

    @JvmStatic
    private fun getLauncher(host: Any): ActivityResultLauncher<Array<String>>? {
        return maps.get(host)
    }

    /**
     * Check if the calling context has a set of permissions.
     *
     * @param context the calling context.
     * @param perms one ore more permissions, such as [Manifest.permission.CAMERA].
     * @return true if all permissions are already granted, false if at least one permission is not
     * yet granted.
     * @see Manifest.permission
     */
    @JvmStatic
    fun hasPermissions(
        context: Context?,
        @Size(min = 1) vararg perms: String
    ): Boolean {
        // Always return true for SDK < M, let the system deal with the permissions
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            Log.w(TAG, "hasPermissions: API version < M, returning true by default")
            return true
        }

        context?.let {
            return perms.all { perm ->
                ContextCompat.checkSelfPermission(it, perm) == PackageManager.PERMISSION_GRANTED
            }
        } ?: run {
            throw IllegalArgumentException("Can't check permissions for null context")
        }
    }

    /**
     * Request a set of permissions, showing a rationale if the system requests it.
     *
     * @param host requesting context.
     * @param rationale a message explaining why the application needs this set of permissions;
     * will be displayed if the user rejects the request the first time.
     * @param perms a set of permissions to be requested.
     * @see Manifest.permission
     */
    @JvmStatic
    fun requestPermissions(
        host: AppCompatActivity,
        rationale: String,
        @Size(min = 1) perms: Array<String>,
        callback: PermissionCallbacks,
    ) {
        val request = PermissionRequest.Builder(host)
            .perms(perms)
            .rationale(rationale)
            .build()
        requestPermissions(host, request, callback)
    }

    /**
     * Request permissions from a Support Fragment with standard OK/Cancel buttons.
     *
     * @see .requestPermissions
     */
    @JvmStatic
    fun requestPermissions(
        host: Fragment,
        rationale: String,
        @Size(min = 1) perms: Array<String>,
        callback: PermissionCallbacks,
    ) {
        val request = PermissionRequest.Builder(host.requireContext())
            .perms(perms)
            .rationale(rationale)
            .build()
        requestPermissions(host, request, callback)
    }

    @Volatile
    private var last: PermissionRequest? = null

    /**
     * Request a set of permissions.
     *
     * @param host requesting context.
     * @param request the permission request
     * @see PermissionRequest
     */
    @JvmStatic
    fun requestPermissions(
        host: Fragment,
        request: PermissionRequest,
        callback: PermissionCallbacks
    ) {
        // Check for permissions before dispatching the request
        this.last = request
        request.callback = callback

        val mHelper = PermissionsHelper.newInstance(host)
        if (mTipType == TipType.Toast) {
            if (hasPermissions(host.requireContext(), *request.perms)) {
                callback.onPermissionsGranted(true, request.perms.toList())
            } else if (mHelper.shouldShowRationale(request.perms)) {
                mIViewProvider.showRationaleDialog(host.requireContext(), request,
                    {
                        callback.onRationaleAccepted()

                        val mTopSnackbar = mHelper.showToastBeforeRequestPermission(request)
                        requestPermissionsReal(mHelper, mTopSnackbar, request, callback)
                    },
                    {
                        callback.onRationaleDenied()
                        callback.onPermissionsDenied(false, request.perms.toList())
                    })
            } else {
                val mTopSnackbar = mHelper.showToastBeforeRequestPermission(request)
                requestPermissionsReal(mHelper, mTopSnackbar, request, callback)
            }
        } else if (mTipType == TipType.Dialog) {
            if (hasPermissions(host.requireContext(), *request.perms)) {
                callback.onPermissionsGranted(true, request.perms.toList())
            } else if (mHelper.shouldShowRationale(request.perms)) {
                mIViewProvider.showRationaleDialog(host.requireContext(), request,
                    {
                        callback.onRationaleAccepted()
                        requestPermissionsReal(mHelper, null, request, callback)
                    },
                    {
                        callback.onRationaleDenied()
                        callback.onPermissionsDenied(false, request.perms.toList())
                    })
            } else {
                mIViewProvider.showConfirmDialog(host.requireContext(), request,
                    {
                        requestPermissionsReal(mHelper, null, request, callback)
                    },
                    {
                        callback.onPermissionsDenied(false, request.perms.toList())
                    })
            }
        }
    }

    private fun requestPermissionsReal(
        helper: PermissionsHelper<*>,
        mTopSnackbar: TopSnackbar? = null,
        request: PermissionRequest,
        callback: PermissionCallbacks
    ) {
        val callback2 = object : PermissionCallbacks {
            override fun onPermissionsGranted(allGranted: Boolean, perms: List<String>) {
                mTopSnackbar?.dismiss()
                callback.onPermissionsGranted(allGranted, perms)
            }

            override fun onPermissionsDenied(doNotAskAgain: Boolean, perms: List<String>) {
                if (helper.somePermissionPermanentlyDenied(perms)) {
                    //永久拒绝，只能跳转到设置界面
                    mIViewProvider.showPermanentlyDeniedDialog(helper.mContext, { mTopSnackbar?.dismiss() },
                        {
                            mTopSnackbar?.dismiss()
                            callback.onPermissionsDenied(false, request.perms.toList())
                        })
                } else {
                    mTopSnackbar?.dismiss()
                    callback.onPermissionsDenied(doNotAskAgain, perms)
                }
            }

            override fun onRationaleAccepted() {
                mTopSnackbar?.dismiss()
                callback.onRationaleAccepted()
            }

            override fun onRationaleDenied() {
                callback.onRationaleDenied()
            }
        }
        request.callback = callback2
        getLauncher(helper.host!!)?.launch(request.perms)
    }

    /**
     * Request a set of permissions.
     *
     * @param host requesting context.
     * @param request the permission request
     * @see PermissionRequest
     */
    @JvmStatic
    fun requestPermissions(
        host: AppCompatActivity,
        request: PermissionRequest,
        callback: PermissionCallbacks
    ) {
        // Check for permissions before dispatching the request
        this.last = request
        request.callback = callback

        val mHelper = PermissionsHelper.newInstance(host)
        if (mTipType == TipType.Toast) {
            if (hasPermissions(host, *request.perms)) {
                callback.onPermissionsGranted(true, request.perms.toList())
            } else if (mHelper.shouldShowRationale(request.perms)) {
                mIViewProvider.showRationaleDialog(host, request,
                    {
                        callback.onRationaleAccepted()

                        val mTopSnackbar = mHelper.showToastBeforeRequestPermission(request)
                        requestPermissionsReal(mHelper, mTopSnackbar, request, callback)
                    },
                    {
                        callback.onRationaleDenied()
                        callback.onPermissionsDenied(false, request.perms.toList())
                    })
            } else {
                val mTopSnackbar = mHelper.showToastBeforeRequestPermission(request)
                requestPermissionsReal(mHelper, mTopSnackbar, request, callback)
            }
        } else if (mTipType == TipType.Dialog) {
            if (hasPermissions(host, *request.perms)) {
                callback.onPermissionsGranted(true, request.perms.toList())
            } else if (mHelper.shouldShowRationale(request.perms)) {
                mIViewProvider.showRationaleDialog(host, request,
                    {
                        callback.onRationaleAccepted()
                        requestPermissionsReal(mHelper, null, request, callback)
                    },
                    {
                        callback.onRationaleDenied()
                        callback.onPermissionsDenied(false, request.perms.toList())
                    })
            } else {
                mIViewProvider.showConfirmDialog(host, request,
                    {
                        requestPermissionsReal(mHelper, null, request, callback)
                    },
                    {
                        callback.onPermissionsDenied(false, request.perms.toList())
                    })
            }
        }
    }

    /**
     * Check if at least one permission in the list of denied permissions has been permanently
     * denied (user clicked "Never ask again").
     *
     * **Note**: Due to a limitation in the information provided by the Android
     * framework permissions API, this method only works after the permission
     * has been denied and your app has received the onPermissionsDenied callback.
     * Otherwise the library cannot distinguish permanent denial from the
     * "not yet denied" case.
     *
     * @param host context requesting permissions.
     * @param deniedPerms list of denied permissions, usually from
     * [PermissionCallbacks.onPermissionsDenied]
     * @return `true` if at least one permission in the list was permanently denied.
     */
    @JvmStatic
    fun somePermissionPermanentlyDenied(
        host: AppCompatActivity,
        deniedPerms: List<String>
    ): Boolean {
        return PermissionsHelper.newInstance(host).somePermissionPermanentlyDenied(deniedPerms)
    }

    /**
     * @see .somePermissionPermanentlyDenied
     */
    @JvmStatic
    fun somePermissionPermanentlyDenied(
        host: Fragment,
        deniedPerms: List<String>
    ): Boolean {
        return PermissionsHelper.newInstance(host).somePermissionPermanentlyDenied(deniedPerms)
    }

    /**
     * See if some denied permission has been permanently denied.
     *
     * @param host requesting context.
     * @param perms array of permissions.
     * @return true if the user has previously denied any of the `perms` and we should show a
     * rationale, false otherwise.
     */
    @JvmStatic
    fun somePermissionDenied(
        host: AppCompatActivity,
        @Size(min = 1) vararg perms: String
    ): Boolean {
        return PermissionsHelper.newInstance(host).somePermissionDenied(perms)
    }

    /**
     * @see .somePermissionDenied
     */
    @JvmStatic
    fun somePermissionDenied(
        host: Fragment,
        @Size(min = 1) vararg perms: String
    ): Boolean {
        return PermissionsHelper.newInstance(host).somePermissionDenied(perms)
    }

    /**
     * Check if a permission has been permanently denied (user clicked "Never ask again").
     *
     * @param host context requesting permissions.
     * @param deniedPerms denied permission.
     * @return `true` if the permissions has been permanently denied.
     */
    @JvmStatic
    fun permissionPermanentlyDenied(
        host: AppCompatActivity,
        deniedPerms: String
    ): Boolean {
        return PermissionsHelper.newInstance(host).permissionPermanentlyDenied(deniedPerms)
    }

    /**
     * @see .permissionPermanentlyDenied
     */
    @JvmStatic
    fun permissionPermanentlyDenied(
        host: Fragment,
        deniedPerms: String
    ): Boolean {
        return PermissionsHelper.newInstance(host).permissionPermanentlyDenied(deniedPerms)
    }
}
