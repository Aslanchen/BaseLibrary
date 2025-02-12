package com.aslan.app.views

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.aslan.app.R
import com.aslan.app.base.ActivityBase
import com.aslan.app.databinding.ActivityPermissionBinding
import com.aslan.baselibrary.listener.setSafeOnClickListener
import com.aslan.baselibrary.permissions.EasyPermissions
import com.aslan.baselibrary.utils.LogUtils

class PermissionActivity : ActivityBase<ActivityPermissionBinding>(ActivityPermissionBinding::inflate) {

    companion object {
        @JvmStatic
        fun newIntent(context: Context): Intent {
            return Intent(context, PermissionActivity::class.java)
        }
    }

    override fun iniBundle(bundle: Bundle) {
    }

    override fun iniView() {
    }

    override fun iniListener() {
        mViewBinding.btDone.setSafeOnClickListener { doRequest() }
    }

    override fun iniData() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.flMain, PermissionFragment.newInstance())
            .commitAllowingStateLoss()
    }

    private fun doRequest() {
        checkAndRequestSDPermission(EasyPermissions.TipType.Toast, object : EasyPermissions.PermissionCallbacks {
            override fun onPermissionsGranted(allGranted: Boolean, perms: List<String>) {
                LogUtils.d("onPermissionsGranted")
            }

            override fun onPermissionsDenied(doNotAskAgain: Boolean, perms: List<String>) {
                LogUtils.d("onPermissionsDenied")
            }
        })
    }
}