package com.aslan.baselibrary.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.aslan.baselibrary.databinding.EmptyViewBinding

/**
 * 空页面
 *
 * @author Aslan
 * @date 2023/04/06
 */
class EmptyView : ConstraintLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    protected val mViewBinding = EmptyViewBinding.inflate(LayoutInflater.from(context), this)

    fun setLayoutResource(@LayoutRes id: Int): View {
        mViewBinding.iv.visibility = GONE
        mViewBinding.tv.visibility = GONE
        mViewBinding.vs.layoutResource = id
        return mViewBinding.vs.inflate()
    }

    fun setImageResource(@DrawableRes resId: Int) {
        mViewBinding.iv.setImageResource(resId)
    }

    fun setImageVisibility(visibility: Int) {
        mViewBinding.iv.visibility = visibility
    }

    fun setTextVisibility(visibility: Int) {
        mViewBinding.tv.visibility = visibility
    }

    fun setText(text: CharSequence?) {
        mViewBinding.tv.text = text
    }

    fun setText(@StringRes resid: Int) {
        mViewBinding.tv.setText(resid)
    }

    fun setTextColor(@ColorInt color: Int) {
        mViewBinding.tv.setTextColor(color)
    }

    fun setTextSize(size: Float) {
        mViewBinding.tv.setTextSize(size)
    }

    fun setTextSize(unit: Int, size: Float) {
        mViewBinding.tv.setTextSize(unit, size)
    }
}
