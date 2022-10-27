package com.aslan.baselibrary.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.viewbinding.ViewBinding
import com.aslan.baselibrary.utils.InflateFragment

/**
 * 基础类
 *
 * @author Aslan
 * @date 2018/4/11
 */
open abstract class VBBaseFragment<VB : ViewBinding>(private val inflate: InflateFragment<VB>) :
    BaseFragment() {
    private var _binding: VB? = null
    protected val mViewBinding get() = _binding!!

    final override fun getLayoutId() = 0

    override fun setCusContentView(inflater: LayoutInflater, container: ViewGroup?): View {
        _binding = inflate.invoke(inflater, container, false)
        return mViewBinding.root
    }

    @CallSuper
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}