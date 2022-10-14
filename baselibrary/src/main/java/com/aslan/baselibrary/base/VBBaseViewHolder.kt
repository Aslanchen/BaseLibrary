package com.aslan.baselibrary.base

import android.view.View
import androidx.viewbinding.ViewBinding
import com.aslan.baselibrary.utils.InflateView
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.viewholders.FlexibleViewHolder

open class VBBaseViewHolder<VB : ViewBinding>(
    view: View,
    adapter: FlexibleAdapter<*>,
    inflate: InflateView<VB>
) : FlexibleViewHolder(view, adapter) {
    private var _binding: VB? = null
    val mViewBinding get() = _binding!!

    init {
        _binding = inflate.invoke(view)
    }
}