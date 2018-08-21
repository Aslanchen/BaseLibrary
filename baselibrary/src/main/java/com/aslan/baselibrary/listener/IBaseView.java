package com.aslan.baselibrary.listener;

import android.support.annotation.StringRes;
import android.support.annotation.UiThread;

/**
 * 错误提示类以及等待框
 *
 * @author Aslan
 * @date 2018/4/11
 */
public interface IBaseView {

  @UiThread
  void showProgressBar();

  @UiThread
  void showProgressBar(@StringRes int msg);

  @UiThread
  void showProgressBar(String msg);

  @UiThread
  void showProgressBar(boolean canCancel);

  @UiThread
  void showProgressBar(boolean canCancel, @StringRes int msg);

  @UiThread
  void showProgressBar(boolean canCancel, String msg);

  @UiThread
  void closeProgressBar();

  @UiThread
  void showToastMessage(@StringRes int resId);

  @UiThread
  void showToastMessage(CharSequence text);

  boolean isAdd();
}