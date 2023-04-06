package com.aslan.baselibrary.base

import androidx.viewbinding.ViewBinding
import com.aslan.baselibrary.utils.InflateFragment
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible

/**
 * 默认使用[FlexibleAdapter]
 *
 * @author Aslan
 * @date 2023/04/06
 */
abstract class VBBaseListSimpleFragment<M, VB : ViewBinding>(inflate: InflateFragment<VB>) :
    VBBaseListFragment<M, FlexibleAdapter<IFlexible<*>>, VB>(inflate) {
    override fun instanceAdapter(): FlexibleAdapter<IFlexible<*>> {
        return FlexibleAdapter(null, this)
    }
}