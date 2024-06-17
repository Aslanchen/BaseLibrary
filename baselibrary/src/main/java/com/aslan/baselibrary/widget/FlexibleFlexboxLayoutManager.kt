package com.aslan.baselibrary.widget

import android.content.Context
import androidx.recyclerview.widget.OrientationHelper
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import eu.davidea.flexibleadapter.common.IFlexibleLayoutManager

/**
 * Flex布局
 *
 * @author Aslanchen
 * @date 2024/6/17
 */
class FlexibleFlexboxLayoutManager : FlexboxLayoutManager, IFlexibleLayoutManager {

    constructor(context: Context) : super(context) {
    }

    constructor(context: Context, @FlexDirection flexDirection: Int) : super(context, flexDirection) {
    }

    constructor(context: Context, @FlexDirection flexDirection: Int, @FlexWrap flexWrap: Int) : super(context, flexDirection, flexWrap) {
    }

    override fun getOrientation(): Int {
        if (flexDirection == FlexDirection.ROW || flexDirection == FlexDirection.ROW_REVERSE) {
            return OrientationHelper.HORIZONTAL
        } else {
            return OrientationHelper.VERTICAL
        }
    }

    override fun getSpanCount(): Int {
        return 1
    }
}