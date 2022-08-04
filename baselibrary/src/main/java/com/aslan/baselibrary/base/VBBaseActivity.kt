package com.aslan.baselibrary.base

import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding

/**
 * 基础类
 *
 * @author Aslan
 * @date 2018/4/11
 */
typealias Inflate2<T> = (LayoutInflater) -> T

abstract class VBBaseActivity<VB : ViewBinding?>(private val inflate: Inflate2<VB>) :
    BaseActivity() {
    private var _binding: VB? = null
    protected val mViewBinding get() = _binding!!

    override fun getLayoutId() = 0

    override fun setCusContentView() {
        _binding = inflate.invoke(layoutInflater)
        setContentView(mViewBinding!!.root)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}