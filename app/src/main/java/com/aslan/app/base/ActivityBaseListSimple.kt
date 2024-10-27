package com.aslan.app.base

import androidx.viewbinding.ViewBinding
import com.aslan.baselibrary.utils.InflateActivity
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible

/**
 * 基础类
 */
abstract class ActivityBaseListSimple<M, VB : ViewBinding>(inflate: InflateActivity<VB>) :
    ActivityBaseList<M, FlexibleAdapter<IFlexible<*>>, VB>(inflate) {
    override fun instanceAdapter(): FlexibleAdapter<IFlexible<*>> {
        return FlexibleAdapter(null, this)
    }
}