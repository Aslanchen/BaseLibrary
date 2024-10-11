package com.aslan.app.base

import androidx.viewbinding.ViewBinding
import com.aslan.baselibrary.base.VBBaseListActivity
import com.aslan.baselibrary.utils.InflateActivity
import com.jaeger.library.StatusBarUtil
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible

abstract class ActivityBaseList<M, A : FlexibleAdapter<IFlexible<*>>, VB : ViewBinding>(inflate: InflateActivity<VB>) :
    VBBaseListActivity<M, A, VB>(inflate) {

    override fun setStatusBar() {
        StatusBarUtil.setTranslucent(this, 0)
        StatusBarUtil.setLightMode(this)
    }

    override fun getPageSize(): Int {
        return 10
    }
}