package com.aslan.baselibrary.base

import android.view.View
import androidx.viewbinding.ViewBinding
import com.aslan.baselibrary.utils.InflateView
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.viewholders.FlexibleViewHolder

/**
 * 配合ViewBinding使用的ViewHolder
 */
open class VBBaseViewHolder<VB : ViewBinding>(
    view: View,
    adapter: FlexibleAdapter<*>,
    inflate: InflateView<VB>,
    stickyHeader: Boolean = false,
) : FlexibleViewHolder(view, adapter, stickyHeader) {
    private var _binding: VB? = null
    val mViewBinding get() = _binding!!

    init {
        _binding = inflate.invoke(view)
    }
}