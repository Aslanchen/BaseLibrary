package com.aslan.app.base

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.viewbinding.ViewBinding
import com.aslan.baselibrary.utils.InflateActivity

/**
 * 基础类
 *
 * @author Aslan
 * @date 2018/4/11
 */
abstract class VBBaseActivity<VB : ViewBinding>(private val inflate: InflateActivity<VB>) : com.aslan.baselibrary.base.VBBaseActivity<VB>(inflate) {
    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //关键代码,沉浸
        WindowCompat.setDecorFitsSystemWindows(window, false)

        val controller = WindowInsetsControllerCompat(window, window.decorView)
//        controller.isAppearanceLightStatusBars = true // 黑色状态栏
        controller.isAppearanceLightStatusBars = false // 白色状态栏
        controller.hide(WindowInsetsCompat.Type.statusBars()) // 状态栏隐藏
        controller.hide(WindowInsetsCompat.Type.navigationBars()) // 导航栏隐藏
        controller.hide(WindowInsetsCompat.Type.systemBars()) // 导航栏隐藏
        controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        //设置专栏栏和导航栏的底色，透明
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = Color.TRANSPARENT
            window.navigationBarColor = Color.TRANSPARENT
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                window.navigationBarDividerColor = Color.TRANSPARENT
            }
        }
    }
}