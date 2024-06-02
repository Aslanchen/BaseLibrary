package com.aslan.baselibrary.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.aslan.baselibrary.utils.InflateFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior

/**
 * 基础类
 *
 * @author Aslan
 * @date 2018/4/11
 */
abstract class VBBaseBottomSheetDialog<VB : ViewBinding>(private val inflate: InflateFragment<VB>) : BaseBottomSheetDialogFragment() {
    private var _binding: VB? = null
    protected val mViewBinding get() = _binding!!

    final override fun getLayoutId() = 0

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

    override fun onStart() {
        super.onStart()
        if (dialog != null) {
            val v = dialog!!.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            BottomSheetBehavior.from<View>(v).apply {
                isHideable = this@VBBaseBottomSheetDialog.isHideable
                isDraggable = this@VBBaseBottomSheetDialog.isDraggable
            }
        }
    }

    open protected var isHideable = true
    open protected var isDraggable = true

    abstract fun iniView()

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}