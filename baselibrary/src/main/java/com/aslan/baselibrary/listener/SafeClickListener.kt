package com.aslan.baselibrary.listener

import android.os.SystemClock
import android.view.View

/**
 * 防止多次点击
 */
fun View.setSafeOnClickListener(onSafeClick: (View) -> Unit) {
    val safeClickListener = SafeClickListener {
        onSafeClick(it)
    }
    setOnClickListener(safeClickListener)
}

class SafeClickListener(
    private var defaultInterval: Int = DEFAUL_TINTERVAL,
    private val onSafeCLick: (View) -> Unit
) : View.OnClickListener {
    companion object {
        var DEFAUL_TINTERVAL = 600
    }

    private var lastTimeClicked = 0L
    override fun onClick(v: View) {
        val now = SystemClock.elapsedRealtime()
        if (now - lastTimeClicked < defaultInterval) {
            return
        }
        lastTimeClicked = now
        onSafeCLick(v)
    }
}