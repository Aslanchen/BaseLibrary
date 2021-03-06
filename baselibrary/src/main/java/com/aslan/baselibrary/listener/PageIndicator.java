package com.aslan.baselibrary.listener;

import androidx.viewpager.widget.ViewPager;

/**
 * Created by Aslan on 2017/12/7.
 */

public interface PageIndicator extends ViewPager.OnPageChangeListener {

  /**
   * Bind the indicator to a ViewPager.
   */
  void setViewPager(ViewPager view);

  /**
   * Bind the indicator to a ViewPager.
   */
  void setViewPager(ViewPager view, int initialPosition);

  /**
   * <p>Set the current page of both the ViewPager and indicator.</p> <p> <p>This
   * <strong>must</strong> be used if you need to set the page before the views are drawn on screen
   * (e.g., default start page).</p>
   */
  void setCurrentItem(int item);

  /**
   * Notify the indicator that the fragment list has changed.
   */
  void notifyDataSetChanged();
}
