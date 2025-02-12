package com.aslan.baselibrary.permissions

import android.content.Context
import com.aslan.baselibrary.permissions.models.PermissionRequest

interface IViewProvider {
    fun showConfirmDialog(context: Context, request: PermissionRequest, agree: () -> Unit, refuse: () -> Unit)
    fun showRationaleDialog(context: Context, request: PermissionRequest, agree: () -> Unit, refuse: () -> Unit)
    fun showPermanentlyDeniedDialog(agree: () -> Unit, refuse: () -> Unit)
}