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
package com.aslan.baselibrary.permissions.models

import android.content.Context
import androidx.annotation.StringRes
import androidx.annotation.StyleRes
import com.aslan.baselibrary.R

/**
 * An immutable model object that holds all of the parameters associated with a permission request,
 * such as the permissions, request code, and rationale.
 *
 * @see PermissionRequest.Builder
 */
data class PermissionRequest(
    @StyleRes
    var theme: Int,
    var code: Int,
    var perms: Array<String>,
    var title: String?,
    var rationale: String?,
    var positiveButtonText: String?,
    var negativeButtonText: String?,
    var onGranted: ((permissions: List<String>, allGranted: Boolean) -> Unit)?,
    var onDenied: ((permissions: List<String>, doNotAskAgain: Boolean) -> Unit)?,
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PermissionRequest

        if (theme != other.theme) return false
        if (code != other.code) return false
        if (!perms.contentEquals(other.perms)) return false
        if (title != other.title) return false
        if (rationale != other.rationale) return false
        if (positiveButtonText != other.positiveButtonText) return false
        if (negativeButtonText != other.negativeButtonText) return false

        return true
    }

    override fun hashCode(): Int {
        var result = theme
        result = 31 * result + code
        result = 31 * result + perms.contentHashCode()
        result = 31 * result + (title?.hashCode() ?: 0)
        result = 31 * result + (rationale?.hashCode() ?: 0)
        result = 31 * result + (positiveButtonText?.hashCode() ?: 0)
        result = 31 * result + (negativeButtonText?.hashCode() ?: 0)
        return result
    }

    /**
     * Builder to build a permission request with variable options.
     *
     * @see PermissionRequest
     */
    @Suppress("UNUSED")
    class Builder(var context: Context?) {
        @StyleRes
        private var theme = 0
        private var code = 0
        private var perms: Array<String> = emptyArray()
        private var title: String? = null
        private var rationale = context?.getString(R.string.rationale_ask)
        private var positiveButtonText = context?.getString(android.R.string.ok)
        private var negativeButtonText = context?.getString(android.R.string.cancel)
        private var onGranted: ((permissions: List<String>, allGranted: Boolean) -> Unit)? = null
        private var onDenied: ((permissions: List<String>, doNotAskAgain: Boolean) -> Unit)? = null

        fun theme(@StyleRes theme: Int) = apply { this.theme = theme }
        fun code(code: Int) = apply { this.code = code }
        fun perms(perms: Array<String>) = apply { this.perms = perms }
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

        fun onGranted(onGranted: (permissions: List<String>, allGranted: Boolean) -> Unit) = apply { this.onGranted = onGranted }
        fun onDenied(onDenied: (permissions: List<String>, doNotAskAgain: Boolean) -> Unit) = apply { this.onDenied = onDenied }

        fun build(): PermissionRequest {
            return PermissionRequest(
                theme = theme,
                code = code,
                perms = perms,
                title = title,
                rationale = rationale,
                positiveButtonText = positiveButtonText,
                negativeButtonText = negativeButtonText,
                onGranted = onGranted,
                onDenied = onDenied,
            )
        }
    }
}