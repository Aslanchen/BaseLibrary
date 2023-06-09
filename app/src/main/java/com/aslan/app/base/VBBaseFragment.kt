package com.aslan.app.base

import androidx.viewbinding.ViewBinding
import com.aslan.baselibrary.utils.InflateFragment

/**
 * 基础类
 *
 * @author Aslan
 * @date 2018/4/11
 */
abstract class VBBaseFragment<VB : ViewBinding>(private val inflate: InflateFragment<VB>) : com.aslan.baselibrary.base.VBBaseFragment<VB>(inflate)