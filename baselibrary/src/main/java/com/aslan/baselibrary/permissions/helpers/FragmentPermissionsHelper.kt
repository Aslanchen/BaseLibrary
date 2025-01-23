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
package com.aslan.baselibrary.permissions.helpers

import android.content.Context
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.aslan.baselibrary.permissions.dialogs.RationaleDialog
import com.aslan.baselibrary.permissions.helpers.base.PermissionsHelper
import com.aslan.baselibrary.permissions.models.PermissionRequest

/**
 * Permissions helper for [Fragment].
 */
internal class FragmentPermissionsHelper(host: Fragment) : PermissionsHelper<Fragment>(host) {

    override var context: Context? = host.requireContext()

    override fun <I, O> registerForActivityResult(
        contract: ActivityResultContract<I, O>,
        callback: ActivityResultCallback<O>
    ): ActivityResultLauncher<I> {
        return host.registerForActivityResult(contract, callback)
    }

    override fun directRequestPermissions(requestCode: Int, perms: Array<String>) {
        val requestPermissionLauncher = host.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
//            if (isGranted) {
//                // PERMISSION GRANTED
//            } else {
//                // PERMISSION NOT GRANTED
//            }

            for (permission in permissions) {

            }
        }
        requestPermissionLauncher.launch(perms)
    }

    override fun shouldShowRequestPermissionRationale(perm: String): Boolean {
        return host.shouldShowRequestPermissionRationale(perm)
    }

    override fun showRequestPermissionRationale(permissionRequest: PermissionRequest) {
        context?.let {
            RationaleDialog(it, permissionRequest).showDialog()
        }
    }
}