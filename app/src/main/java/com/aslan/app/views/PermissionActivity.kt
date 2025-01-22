package com.aslan.app.views

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.aslan.app.base.ActivityBase
import com.aslan.app.databinding.ActivityPermissionBinding
import com.aslan.baselibrary.listener.setSafeOnClickListener

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
    }

    private fun doRequest() {
        checkAndRequestSDPermission()
    }
}