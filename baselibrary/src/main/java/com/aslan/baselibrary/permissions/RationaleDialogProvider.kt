package com.aslan.baselibrary.permissions

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.aslan.baselibrary.permissions.models.PermissionRequest

class RationaleDialogProvider : IViewProvider {
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
}