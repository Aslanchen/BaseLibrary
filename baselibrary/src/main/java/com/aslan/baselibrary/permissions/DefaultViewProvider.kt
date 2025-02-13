package com.aslan.baselibrary.permissions

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import com.aslan.baselibrary.R
import com.aslan.baselibrary.permissions.models.PermissionRequest

open class DefaultViewProvider : IViewProvider {
    override fun showConfirmDialog(context: Context, request: PermissionRequest, agree: () -> Unit, refuse: () -> Unit) {
        AlertDialog.Builder(context)
            .setCancelable(false)
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

    override fun showRationaleDialog(context: Context, request: PermissionRequest, agree: () -> Unit, refuse: () -> Unit) {
        AlertDialog.Builder(context)
            .setCancelable(false)
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

    override fun showPermanentlyDeniedDialog(context: Context, agree: () -> Unit, refuse: () -> Unit) {
        AlertDialog.Builder(context)
            .setCancelable(false)
            .setTitle(R.string.permissions)
            .setMessage(R.string.request_permission_permanently_denied)
            .setPositiveButton(R.string.go_setting) { dialog, which ->
                agree()

                gotoSetting(context)
            }
            .setNegativeButton(R.string.refuse) { dialog, which ->
                refuse()
            }
            .show()
    }

    open protected fun gotoSetting(context: Context) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", context.packageName, null)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }
}