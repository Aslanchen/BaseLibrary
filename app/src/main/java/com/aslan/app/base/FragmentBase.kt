package com.aslan.app.base

import androidx.viewbinding.ViewBinding
import com.aslan.baselibrary.base.VBBaseFragment
import com.aslan.baselibrary.utils.InflateFragment

/**
 * 基础类
 */
abstract class FragmentBase<VB : ViewBinding>(inflate: InflateFragment<VB>) : VBBaseFragment<VB>(inflate) {

}