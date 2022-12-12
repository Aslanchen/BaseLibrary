package com.aslan.baselibrary.listener

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.DefaultLifecycleObserver

/**
 * MPV基础类
 *
 * @author Aslan
 * @date 2018/4/11
 */
interface IMVPBasePresenter : DefaultLifecycleObserver {
    fun iniBundle(bundle: Bundle)
    fun iniData()
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    )

    fun getFragmentManager(): FragmentManager
}