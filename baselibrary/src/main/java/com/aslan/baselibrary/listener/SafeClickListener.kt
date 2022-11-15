package com.aslan.baselibrary.listener

import android.os.SystemClock
import android.view.View

fun View.setSafeOnClickListener(onSafeClick: (View) -> Unit) {
    val safeClickListener = SafeClickListener {
        onSafeClick(it)
    }
    setOnClickListener(safeClickListener)
}

class SafeClickListener(
    private var defaultInterval: Int = 1500,
    private val onSafeCLick: (View) -> Unit
) : View.OnClickListener {
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