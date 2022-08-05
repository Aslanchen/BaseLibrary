package com.aslan.baselibrary.base

import androidx.viewbinding.ViewBinding
import com.aslan.baselibrary.utils.InflateActivity

/**
 * 基础类
 *
 * @author Aslan
 * @date 2018/4/11
 */
abstract class VBBaseActivity<VB : ViewBinding>(private val inflate: InflateActivity<VB>) :
    BaseActivity() {
    private var _binding: VB? = null
    protected val mViewBinding get() = _binding!!

    override fun getLayoutId() = 0

    override fun setCusContentView() {
        _binding = inflate.invoke(layoutInflater)
        setContentView(mViewBinding.root)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}