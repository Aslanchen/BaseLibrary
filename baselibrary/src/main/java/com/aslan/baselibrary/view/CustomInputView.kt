package com.aslan.baselibrary.view

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.text.Editable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.EditText
import com.aslan.baselibrary.R

class CustomInputView : CustomLabelView {
    constructor(context: Context) : super(context) {
        init(null, 0, 0);
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs, 0, 0)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(attrs, defStyleAttr, 0)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(attrs, defStyleAttr, defStyleRes)
    }

    private fun init(attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        if (attrs != null) {
            val a = context.obtainStyledAttributes(
                attrs,
                R.styleable.CustomeInputView,
                defStyleAttr,
                defStyleRes
            )
            a.recycle()
        }
    }

    override fun getValueText(): Editable {
        return getEditText().text
    }

    fun getEditText(): EditText {
        return getValueTextView() as EditText
    }

    fun getValueHintText(): CharSequence {
        return getEditText().hint
    }

    override fun initLayout() {
        LayoutInflater.from(context).inflate(R.layout.layout_custome_input_view, this)
    }

    fun requestInputFocus() {
        getEditText().requestFocus()
    }
}