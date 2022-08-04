package com.aslan.baselibrary.base

import android.content.Intent
import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.viewbinding.ViewBinding
import com.aslan.baselibrary.listener.IMVPBasePresenter
import com.aslan.baselibrary.utils.InflateActivity

/**
 * MPV基础类
 *
 * @author Aslan
 * @date 2018/4/11
 */
abstract class MVPBaseActivity<VB : ViewBinding, P : IMVPBasePresenter>(inflate: InflateActivity<VB>) :
    VBBaseActivity<VB>(inflate) {
    abstract fun iniPresenter(): P

    protected var mPresenter = this.iniPresenter()

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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