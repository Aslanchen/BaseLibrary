package com.aslan.baselibrary.view

import android.content.Context
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import java.lang.Integer.max

/**
 * 数字输入EditText，可以设置最大值和最小值。
 *
 * @author Aslan
 * @date 2023/04/06
 */
class NumberEditText : androidx.appcompat.widget.AppCompatEditText {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

//    constructor(
//        context: Context,
//        attrs: AttributeSet?,
//        defStyleAttr: Int = 0,
//        defStyleRes: Int = 0
//    ) : super(context, attrs, defStyleAttr, defStyleRes)

    private val mTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(s: Editable) {
            val input = s.toString().trim().toIntOrNull()
            if (input != null) {
                if (input > max) {
                    setText(max.toString())
                    setSelection(text!!.length)
                    return
                }

                if (input < min) {
                    setText(min.toString())
                    setSelection(text!!.length)
                    return
                }
            }
        }
    }

    init {
        inputType = InputType.TYPE_CLASS_NUMBER
        setSingleLine()
        maxLines = 1
        setLines(1)
    }

    var min = 0
    var max = 0
    fun setRangeNumber(min: Int, max: Int) {
        this.min = min
        this.max = max
        val maxLen = max(min.toString().length, max.toString().length)
        setFilters(arrayOf(InputFilter.LengthFilter(maxLen)))
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        addTextChangedListener(mTextWatcher)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        removeTextChangedListener(mTextWatcher)
    }
}