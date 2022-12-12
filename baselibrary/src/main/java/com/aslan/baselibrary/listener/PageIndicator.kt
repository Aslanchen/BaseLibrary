package com.aslan.baselibrary.listener

import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener

/**
 * Created by Aslan on 2017/12/7.
 */
interface PageIndicator : OnPageChangeListener {
    /**
     * Bind the indicator to a ViewPager.
     */
    fun setViewPager(view: ViewPager)

    /**
     * Bind the indicator to a ViewPager.
     */
    fun setViewPager(view: ViewPager, initialPosition: Int)

    /**
     *
     * Set the current page of both the ViewPager and indicator.
     *
     *
     *
     *This
     * **must** be used if you need to set the page before the views are drawn on screen
     * (e.g., default start page).
     */
    fun setCurrentItem(item: Int)

    /**
     * Notify the indicator that the fragment list has changed.
     */
    fun notifyDataSetChanged()
}