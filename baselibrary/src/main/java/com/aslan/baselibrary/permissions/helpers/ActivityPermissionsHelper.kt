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
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.aslan.baselibrary.permissions.helpers.base.PermissionsHelper
import com.aslan.baselibrary.permissions.models.PermissionRequest
import com.aslan.baselibrary.widget.TopSnackbar

/**
 * Permissions helper for [AppCompatActivity].
 */
internal class ActivityPermissionsHelper(host: AppCompatActivity) : PermissionsHelper<AppCompatActivity>(host) {

    override var mContext: Context = host
    override fun showToastBeforeRequestPermission(request: PermissionRequest): TopSnackbar {
        val viewGroup = host.findViewById<ViewGroup>(android.R.id.content)
        val mTopSnackbar = TopSnackbar.make(viewGroup, request.title ?: "", request.rationale ?: "")
        mTopSnackbar.show()
        return mTopSnackbar
    }

    override fun shouldShowRequestPermissionRationale(perm: String): Boolean {
        return ActivityCompat.shouldShowRequestPermissionRationale(host, perm)
    }
}
