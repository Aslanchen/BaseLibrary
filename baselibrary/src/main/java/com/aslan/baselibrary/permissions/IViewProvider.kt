package com.aslan.baselibrary.permissions

import android.content.Context
import com.aslan.baselibrary.permissions.models.PermissionRequest

interface IViewProvider {

    /**
     * 申请权限之前，弹出确认框
     * [EasyPermissions.mTipType] 为[EasyPermissions.TipType.Dialog]时候有效
     */
    fun showConfirmDialog(context: Context, request: PermissionRequest, agree: () -> Unit, refuse: () -> Unit)

    /**
     * 第一次拒绝后，再次调用[requestPermissions]弹出确认框
     */
    fun showRationaleDialog(context: Context, request: PermissionRequest, agree: () -> Unit, refuse: () -> Unit)

    /**
     * 多次拒绝，或者勾选不再提示后触发的弹框
     */
    fun showPermanentlyDeniedDialog(context: Context, agree: () -> Unit, refuse: () -> Unit)
}