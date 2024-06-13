package com.aslan.baselibrary.view

import android.annotation.TargetApi
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.InputFilter
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import com.aslan.baselibrary.R


open class CustomLabelView : ConstraintLayout {
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

    private var startMargin = 0
    private var topMargin = 0
    private var label_width = 0

    private var paddingVertical = -1
    private var paddingHorizontal = -1
    private var topPadding = -1
    private var bottomPadding = -1
    private var startPadding = -1
    private var endPadding = -1

    private var mDrawablePadding = 0
    private var drawableTop: Drawable? = null
    private var drawableBottom: Drawable? = null
    private var drawableStart: Drawable? = null
    private var drawableEnd: Drawable? = null

    private var tvLabel: TextView? = null
    private var tvValue: TextView? = null

    private fun init(attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        initLayout()

        if (attrs != null) {
            val a = context.obtainStyledAttributes(
                attrs,
                R.styleable.CustomeLabelView,
                defStyleAttr,
                defStyleRes
            )

            paddingVertical =
                a.getDimensionPixelSize(R.styleable.CustomeLabelView_value_paddingVertical, -1)
            paddingHorizontal =
                a.getDimensionPixelSize(R.styleable.CustomeLabelView_value_paddingHorizontal, -1)

            startMargin = a.getDimensionPixelSize(R.styleable.CustomeLabelView_marginStart, 0)
            topMargin = a.getDimensionPixelSize(R.styleable.CustomeLabelView_marginTop, 0)
            label_width =
                a.getLayoutDimension(
                    R.styleable.CustomeLabelView_label_layout_width,
                    "label_layout_width"
                )

            val singleLine = a.getBoolean(R.styleable.CustomeLabelView_singleLine, false)
            val lines = a.getInt(R.styleable.CustomeLabelView_lines, -1)
            getValueTextView().isSingleLine = singleLine
            if (lines != -1) {
                getValueTextView().setLines(lines)
            }

            mDrawablePadding =
                a.getDimensionPixelSize(R.styleable.CustomeLabelView_drawablePadding, 0)
            drawableStart = a.getDrawable(R.styleable.CustomeLabelView_drawableStart)
            getLabelTextView().setCompoundDrawablesRelativeWithIntrinsicBounds(
                drawableStart,
                null,
                null,
                null
            )
            getLabelTextView().compoundDrawablePadding = mDrawablePadding

            drawableEnd = a.getDrawable(R.styleable.CustomeLabelView_drawableEnd)
            getValueTextView().setCompoundDrawablesRelativeWithIntrinsicBounds(
                null,
                null,
                drawableEnd,
                null
            )
            getValueTextView().compoundDrawablePadding = mDrawablePadding

            val orientation = a.getInt(R.styleable.CustomeLabelView_orientation, 0)
            setOrientation(orientation)

            val labelTextSize = a.getDimension(R.styleable.CustomeLabelView_labelTextSize, 15F)
            getLabelTextView().setTextSize(TypedValue.COMPLEX_UNIT_PX, labelTextSize)

            val valueTextSize = a.getDimension(R.styleable.CustomeLabelView_valueTextSize, 15F)
            getValueTextView().setTextSize(TypedValue.COMPLEX_UNIT_PX, valueTextSize)

            if (a.hasValue(R.styleable.CustomeLabelView_labelTextColor)) {
                val textColor = a.getColorStateList(R.styleable.CustomeLabelView_labelTextColor)!!
                setLabelColor(textColor)
            }

            if (a.hasValue(R.styleable.CustomeLabelView_valueTextColor)) {
                val textColor = a.getColorStateList(R.styleable.CustomeLabelView_valueTextColor)!!
                setValueColor(textColor)
            }

            if (a.hasValue(R.styleable.CustomeLabelView_hintTextColor)) {
                val hintTextColor =
                    a.getColorStateList(R.styleable.CustomeLabelView_hintTextColor)!!
                setHintTextColor(hintTextColor)
            }

            if (a.hasValue(R.styleable.CustomeLabelView_labelText)) {
                val text = a.getText(R.styleable.CustomeLabelView_labelText)
                getLabelTextView().setText(text)
            }

            if (a.hasValue(R.styleable.CustomeLabelView_valueText)) {
                val text = a.getText(R.styleable.CustomeLabelView_valueText)
                getValueTextView().setText(text)
            }

            if (a.hasValue(R.styleable.CustomeLabelView_hint)) {
                val text = a.getText(R.styleable.CustomeLabelView_hint)
                getValueTextView().setHint(text)
            }

            if (a.hasValue(R.styleable.CustomeLabelView_labelGravity)) {
                val gravity = a.getInt(R.styleable.CustomeLabelView_labelGravity, -1)
                getLabelTextView().gravity = gravity
            }

            if (a.hasValue(R.styleable.CustomeLabelView_valueGravity)) {
                val gravity = a.getInt(R.styleable.CustomeLabelView_valueGravity, -1)
                getValueTextView().gravity = gravity
            }

            val inputType = a.getInt(R.styleable.CustomeLabelView_inputType, EditorInfo.TYPE_NULL)
            if (inputType != EditorInfo.TYPE_NULL) {
                getValueTextView().inputType = inputType
            }

            if (a.hasValue(R.styleable.CustomeLabelView_value_background)) {
                val background = a.getDrawable(R.styleable.CustomeLabelView_value_background)
                getValueTextView().background = background
            } else {
                getValueTextView().background = null
            }

            val maxlength = a.getInt(R.styleable.CustomeLabelView_maxLength, -1)
            if (maxlength >= 0) {
                getValueTextView().filters = arrayOf(InputFilter.LengthFilter(maxlength))
            }

            if (paddingHorizontal >= 0) {
                startPadding = paddingHorizontal
                endPadding = paddingHorizontal
            }
            if (paddingVertical >= 0) {
                topPadding = paddingVertical
                bottomPadding = paddingVertical
            }
            getValueTextView().setPadding(startPadding, topPadding, endPadding, bottomPadding)

            val layout_constraintHorizontal_bias =
                a.getFloat(R.styleable.CustomeLabelView_value_layout_constraintHorizontal_bias, 1F)
            val lpValue = getValueTextView().layoutParams as ConstraintLayout.LayoutParams
            lpValue.horizontalBias = layout_constraintHorizontal_bias

            if (a.hasValue(R.styleable.CustomeLabelView_labelFontFamily)) {
                val fontResId = a.getResourceId(R.styleable.CustomeLabelView_labelFontFamily, 0)
                getLabelTextView().setTypeface(ResourcesCompat.getFont(context, fontResId))
            }

            if (a.hasValue(R.styleable.CustomeLabelView_valueFontFamily)) {
                val fontResId = a.getResourceId(R.styleable.CustomeLabelView_valueFontFamily, 0)
                getValueTextView().setTypeface(ResourcesCompat.getFont(context, fontResId))
            }

            if (a.hasValue(R.styleable.CustomeLabelView_label_textStyle)) {
                val labelTextStyle = a.getInt(R.styleable.CustomeLabelView_label_textStyle, 0)
                getLabelTextView().setTypeface(getLabelTextView().typeface, labelTextStyle)
            }

            if (a.hasValue(R.styleable.CustomeLabelView_value_textStyle)) {
                val valueTextStyle = a.getInt(R.styleable.CustomeLabelView_value_textStyle, 0)
                getValueTextView().setTypeface(getValueTextView().typeface, valueTextStyle)
            }

            a.recycle()
        }
    }

    protected open fun initLayout() {
        LayoutInflater.from(context).inflate(R.layout.layout_custome_label_view, this)
    }

    /**
     * @param orientation 0-horizontal, 1-vertical
     */
    fun setOrientation(orientation: Int) {
        val lpLabel = getLabelTextView().layoutParams as ConstraintLayout.LayoutParams
        val lpValue = getValueTextView().layoutParams as ConstraintLayout.LayoutParams
        if (orientation == 0) {
            //还原属性
            lpLabel.bottomToTop = LayoutParams.UNSET
            lpValue.startToStart = LayoutParams.UNSET

            //设置新属性
            lpLabel.startToStart = LayoutParams.PARENT_ID
            lpLabel.topToTop = LayoutParams.PARENT_ID
            lpLabel.endToEnd = LayoutParams.UNSET
//            lpLabel.bottomToBottom = LayoutParams.PARENT_ID
            lpLabel.height = LayoutParams.WRAP_CONTENT
            lpLabel.width = label_width

            lpValue.marginStart = startMargin
            lpValue.topMargin = topMargin
            lpValue.startToEnd = getLabelTextView().id
            lpValue.topToTop = LayoutParams.PARENT_ID
            lpValue.endToEnd = LayoutParams.PARENT_ID
//            lpValue.bottomToBottom = LayoutParams.PARENT_ID
            lpValue.height = LayoutParams.WRAP_CONTENT
            lpValue.width = LayoutParams.WRAP_CONTENT
        } else {
            //还原属性
            lpLabel.bottomToBottom = LayoutParams.UNSET
            lpValue.startToEnd = LayoutParams.UNSET

            //设置新属性
            lpLabel.startToStart = LayoutParams.PARENT_ID
            lpLabel.topToTop = LayoutParams.PARENT_ID
            lpLabel.endToEnd = LayoutParams.UNSET
            lpLabel.bottomToTop = getValueTextView().id
            lpLabel.height = LayoutParams.WRAP_CONTENT
            lpLabel.width = label_width

            lpValue.marginStart = startMargin
            lpValue.topMargin = topMargin
            lpValue.startToStart = LayoutParams.PARENT_ID
            lpValue.topToTop = LayoutParams.UNSET
            lpValue.topToBottom = getLabelTextView().id
            lpValue.endToEnd = LayoutParams.UNSET
            lpValue.bottomToBottom = LayoutParams.PARENT_ID
            lpValue.height = LayoutParams.WRAP_CONTENT
            lpValue.width = LayoutParams.MATCH_PARENT
        }
    }

    fun setLabelAndValue(@StringRes v1: Int, @StringRes v2: Int) {
        setLabelText(v1)
        setValueText(v2)
    }

    fun setLabelAndValue(@StringRes v1: Int, v2: String? = "") {
        setLabelText(v1)
        setValueText(v2)
    }

    fun setLabelAndValue(v1: String? = "", v2: String? = "") {
        setLabelText(v1)
        setValueText(v2)
    }

    fun setLabelAndValue(v1: String? = "", @StringRes v2: Int) {
        setLabelText(v1)
        setValueText(v2)
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        getValueTextView().isEnabled = enabled
    }

    fun getHint(): CharSequence {
        return getValueTextView().hint
    }

    open fun getValueText(): CharSequence {
        return getValueTextView().text
    }

    fun setLabelText(text: CharSequence? = "") {
        getLabelTextView().setText(text)
    }

    fun setLabelText(@StringRes resId: Int) {
        getLabelTextView().setText(resId)
    }

    fun setValueText(text: CharSequence? = "") {
        getValueTextView().setText(text)
    }

    fun setValueText(@StringRes resId: Int) {
        getValueTextView().setText(resId)
    }

    fun setLabelTextSize(size: Float) {
        getLabelTextView().setTextSize(size)
    }

    fun setValueTextSize(size: Float) {
        getValueTextView().setTextSize(size)
    }

    fun setLabelColor(@ColorInt color: Int) {
        getLabelTextView().setTextColor(color)
    }

    fun setLabelColor(colors: ColorStateList) {
        getLabelTextView().setTextColor(colors)
    }

    fun setValueColor(@ColorInt color: Int) {
        getValueTextView().setTextColor(color)
    }

    fun setValueColor(colors: ColorStateList) {
        getValueTextView().setTextColor(colors)
    }

    fun setHintTextColor(@ColorInt color: Int) {
        getValueTextView().setHintTextColor(color)
    }

    fun setHintTextColor(colors: ColorStateList) {
        getValueTextView().setHintTextColor(colors)
    }

    fun clearAll() {
        getLabelTextView().setText("")
        getValueTextView().setText("")
    }

    fun clearValue() {
        getValueTextView().setText("")
    }

    fun addTextChangedListener(watcher: TextWatcher) {
        getValueTextView().addTextChangedListener(watcher)
    }

    fun removeTextChangedListener(watcher: TextWatcher) {
        getValueTextView().removeTextChangedListener(watcher)
    }

    fun getLabelTextView(): TextView {
        if (tvLabel == null) {
            tvLabel = findViewById(R.id.tvLabel)
        }
        return tvLabel!!
    }

    fun getValueTextView(): TextView {
        if (tvValue == null) {
            tvValue = findViewById(R.id.tvValue)
        }
        return tvValue!!
    }

    fun setDrawableEnd(drawableEnd: Drawable?) {
        this.drawableEnd = drawableEnd
        getValueTextView().setCompoundDrawablesRelativeWithIntrinsicBounds(
            null,
            null,
            drawableEnd,
            null
        )
    }
}