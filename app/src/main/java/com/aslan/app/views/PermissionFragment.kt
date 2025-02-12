package com.aslan.app.views

import android.os.Bundle
import android.view.View
import com.aslan.app.base.FragmentBase
import com.aslan.app.databinding.FragmentPermissionBinding
import com.aslan.baselibrary.listener.setSafeOnClickListener
import com.aslan.baselibrary.permissions.EasyPermissions
import com.aslan.baselibrary.permissions.models.PermissionRequest
import com.aslan.baselibrary.utils.LogUtils

class PermissionFragment : FragmentBase<FragmentPermissionBinding>(FragmentPermissionBinding::inflate) {

    companion object {
        fun newInstance(): PermissionFragment {
            val args = Bundle()
            val fragment = PermissionFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun iniBundle(bundle: Bundle) {
    }

    override fun iniView(view: View) {
    }

    override fun iniListener() {
        mViewBinding.btDone.setSafeOnClickListener { doRequest() }
    }

    override fun iniData() {
    }

    private fun doRequest() {
        EasyPermissions.mTipType = EasyPermissions.TipType.Dialog
        checkAndRequestPermission(
            PermissionRequest.Builder(requireContext())
                .title(com.aslan.baselibrary.R.string.permissions)
                .perms(EasyPermissions.PERMISSIONS_LOCATION)
                .rationale("定位需要，申请权限")
                .positiveButtonText("去授权")
                .negativeButtonText("取消")
                .build(), object : EasyPermissions.SimplePermissionCallbacks() {
                override fun onPermissionsGranted(allGranted: Boolean, perms: List<String>) {
                    LogUtils.d("onPermissionsGranted")
                }

                override fun onPermissionsDenied(doNotAskAgain: Boolean, perms: List<String>) {
                    LogUtils.d("onPermissionsDenied")
                }
            }
        )
    }
}