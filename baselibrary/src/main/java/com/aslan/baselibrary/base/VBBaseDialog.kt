package com.aslan.baselibrary.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.aslan.baselibrary.utils.InflateFragment

/**
 * 基础类
 *
 * @author Aslan
 * @date 2018/4/11
 */
abstract class VBBaseDialog<VB : ViewBinding>(private val inflate: InflateFragment<VB>) :
    BaseDialogFragment() {
    private var _binding: VB? = null
    protected val mViewBinding get() = _binding!!

    override fun getLayoutId() = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflate.invoke(inflater, container, false)
        return mViewBinding.root
    }

    override fun iniView(view: View) {
        iniView()
    }

    abstract fun iniView()

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}