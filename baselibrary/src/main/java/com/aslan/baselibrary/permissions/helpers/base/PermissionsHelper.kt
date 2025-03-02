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
package com.aslan.baselibrary.permissions.helpers.base

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.aslan.baselibrary.permissions.helpers.ActivityPermissionsHelper
import com.aslan.baselibrary.permissions.helpers.FragmentPermissionsHelper
import com.aslan.baselibrary.permissions.models.PermissionRequest
import com.aslan.baselibrary.widget.TopSnackbar

/**
 * Delegate class to make permission calls based on the 'host' (Fragment, Activity, etc).
 */
abstract class PermissionsHelper<T>(val host: T) {

    companion object {

        fun newInstance(host: AppCompatActivity): PermissionsHelper<out Activity> {
            return ActivityPermissionsHelper(host)
        }

        fun newInstance(host: Fragment): PermissionsHelper<Fragment> {
            return FragmentPermissionsHelper(host)
        }
    }

    abstract var mContext: Context

    abstract fun showToastBeforeRequestPermission(request: PermissionRequest): TopSnackbar
    abstract fun shouldShowRequestPermissionRationale(perm: String): Boolean

    fun somePermissionPermanentlyDenied(perms: List<String>): Boolean {
        return perms.any { permissionPermanentlyDenied(it) }
    }

    fun permissionPermanentlyDenied(perm: String): Boolean {
        return !shouldShowRequestPermissionRationale(perm)
    }

    fun somePermissionDenied(perms: Array<out String>): Boolean {
        return shouldShowRationale(perms)
    }

    fun shouldShowRationale(perms: Array<out String>): Boolean {
        return perms.any { shouldShowRequestPermissionRationale(it) }
    }
}
