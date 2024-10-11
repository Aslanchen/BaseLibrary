package com.aslan.app.base

import androidx.viewbinding.ViewBinding
import com.aslan.baselibrary.base.VBBaseListFragment
import com.aslan.baselibrary.utils.InflateFragment
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible

abstract class FragmentBaseList<M, A : FlexibleAdapter<IFlexible<*>>, VB : ViewBinding>(inflate: InflateFragment<VB>) :
    VBBaseListFragment<M, A, VB>(inflate) {

    override fun getPageSize(): Int {
        return 10
    }
}