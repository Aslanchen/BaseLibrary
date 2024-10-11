package com.aslan.app.base

import androidx.viewbinding.ViewBinding
import com.aslan.baselibrary.utils.InflateFragment
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible

abstract class FragmentBaseListSimple<M, VB : ViewBinding>(inflate: InflateFragment<VB>) :
    FragmentBaseList<M, FlexibleAdapter<IFlexible<*>>, VB>(inflate) {
    override fun instanceAdapter(): FlexibleAdapter<IFlexible<*>> {
        return FlexibleAdapter(null, this)
    }
}