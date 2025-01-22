package com.aslan.baselibrary.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.aslan.baselibrary.databinding.TopSnackbarViewBinding
import com.blankj.utilcode.util.SizeUtils
import com.google.android.material.snackbar.ContentViewCallback

class TopSnackbarView : ConstraintLayout, ContentViewCallback {
    constructor(context: Context) : super(context) {
        init(null, 0, 0);
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs, 0, 0)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs, defStyleAttr, 0)
    }

    private var mViewBinding = TopSnackbarViewBinding.inflate(LayoutInflater.from(context), this)

    private fun init(attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        if (attrs != null) {
        }

        fitsSystemWindows = true
        val padding = SizeUtils.dp2px(20f)
        setPadding(padding, 0, padding, 0)
    }

    override fun animateContentIn(delay: Int, duration: Int) {
    }

    override fun animateContentOut(delay: Int, duration: Int) {
    }

    fun setTitle(title: String) {
        mViewBinding.tvName.setText(title)
    }

    fun setContent(content: String) {
        mViewBinding.tvContent.setText(content)
    }
}