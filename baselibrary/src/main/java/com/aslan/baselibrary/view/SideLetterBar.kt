package com.aslan.baselibrary.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Paint.Align
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.ColorInt
import com.aslan.baselibrary.utils.AppUtil

/**
 * 快捷列表
 *
 * @author Aslanchen
 * @date 2024/6/10
 */
class SideLetterBar : View {
    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context)
    }

    private var paint: Paint? = null
    private var letterWidth = 0
    private var letterHeight = 0f
    private var textColor = Color.WHITE
    private var textColorSelected = Color.BLACK
    private var mSideLetter = mutableListOf(
        "#", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z",
    )

    private fun init(context: Context) {
        paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint!!.setColor(Color.WHITE)
        paint!!.setTextSize(AppUtil.dip2px(context, 16F).toFloat())
        paint!!.setTextAlign(Align.CENTER)
    }

    fun setTextSize(dp: Float) {
        paint!!.setTextSize(AppUtil.dip2px(context, dp).toFloat())
    }

    fun setTextColor(@ColorInt color: Int) {
        this.textColor = color
        paint!!.setColor(color)
    }

    fun setTextColorSelected(@ColorInt color: Int) {
        this.textColorSelected = color
    }

    fun setList(list: MutableList<String>) {
        this.mSideLetter = list
        invalidate()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        letterWidth = measuredWidth
        //得到一个字母要显示的高度
        letterHeight = measuredHeight * 1f / mSideLetter.size
    }

    private var lastIndex = -1 //标记上次的触摸字母的索引

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        for (i in 0 until mSideLetter.size) {
            val x = letterWidth / 2F
            val y = (letterHeight / 2F) + (getTextHeight(mSideLetter[i]) / 2F) + (i * letterHeight)

            paint!!.color = if (lastIndex == i) textColorSelected else textColor

            canvas!!.drawText(mSideLetter[i], x, y, paint!!)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                val touchY = event.y
                val index = (touchY / letterHeight).toInt() //得到字母对应的索引
                if (lastIndex != index) {
                    //判断当前触摸字母跟上次触摸的是不是同一个字母
                    if (index >= 0 && index < mSideLetter.size) {
                        //判断触摸的范围是否在所有字母的高度之内
                        listener?.onTouchLetter(mSideLetter[index])
                    }
                }
                lastIndex = index
            }

            MotionEvent.ACTION_UP -> {
                //滑动触目事件消失，标记的位置索引就要进行重置
                lastIndex = -1
            }
        }

        //刷新界面进行从新绘制
        invalidate()
        return true
    }

    /**
     * 获取文本的高度
     * @param text
     * @return
     */
    private fun getTextHeight(text: String): Int {
        //获取文本的高度
        val bounds = Rect()
        paint!!.getTextBounds(text, 0, text.length, bounds)
        return bounds.height()
    }

    private var listener: OnTouchLetterListener? = null
    fun setOnTouchLetterListener(listener: OnTouchLetterListener?) {
        this.listener = listener
    }

    /**
     * 触摸字母的监听事件
     *
     */
    interface OnTouchLetterListener {
        fun onTouchLetter(letter: String?)
    }
}