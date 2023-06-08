package com.aslan.app.base

import androidx.viewbinding.ViewBinding
import com.aslan.baselibrary.utils.InflateActivity

/**
 * 基础类
 *
 * @author Aslan
 * @date 2018/4/11
 */
abstract class VBBaseActivity<VB : ViewBinding>(private val inflate: InflateActivity<VB>) : com.aslan.baselibrary.base.VBBaseActivity<VB>(inflate)