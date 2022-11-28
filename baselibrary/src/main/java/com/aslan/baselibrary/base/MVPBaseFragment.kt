package com.aslan.baselibrary.base

import android.content.Intent
import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.viewbinding.ViewBinding
import com.aslan.baselibrary.listener.IMVPBasePresenter
import com.aslan.baselibrary.utils.InflateFragment

/**
 * MPV基础类
 *
 * @author Aslan
 * @date 2018/4/11
 */
abstract class MVPBaseFragment<VB : ViewBinding, P : IMVPBasePresenter>(inflate: InflateFragment<VB>) :
    VBBaseFragment<VB>(inflate) {
    abstract fun iniPresenter(): P

    lateinit var mPresenter: P

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPresenter = this.iniPresenter()
        lifecycle.addObserver(mPresenter)
    }

    @CallSuper
    override fun iniData() {
        mPresenter.iniData()
    }

    @CallSuper
    override fun iniBundle(bundle: Bundle) {
        mPresenter.iniBundle(bundle)
    }

    @CallSuper
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        mPresenter.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    @CallSuper
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        mPresenter.onRequestPermissionsResult(requestCode, permissions, grantResults)
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}