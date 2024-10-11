package com.aslan.app.base

import androidx.viewbinding.ViewBinding
import com.aslan.baselibrary.base.VBBaseActivity
import com.aslan.baselibrary.utils.InflateActivity
import com.jaeger.library.StatusBarUtil

/**
 * 基础类
 */
abstract class ActivityBase<VB : ViewBinding>(inflate: InflateActivity<VB>) : VBBaseActivity<VB>(inflate) {

    companion object {
    }

    override fun setStatusBar() {
        StatusBarUtil.setTranslucent(this, 0)
        StatusBarUtil.setLightMode(this)
    }
}