package com.aslan.baselibrary.listener;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;
import android.support.annotation.StringRes;
import android.support.annotation.UiThread;
import com.aslan.baselibrary.http.BaseHttpError;

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
  void showProgressBar(@NonNull String msg);

  @UiThread
  void showProgressBar(boolean canCancel);

  @UiThread
  void showProgressBar(boolean canCancel, @StringRes int msg);

  @UiThread
  void showProgressBar(boolean canCancel, @NonNull String msg);

  @UiThread
  void closeProgressBar();

  @UiThread
  void showToastMessage(@StringRes int resId);

  @UiThread
  void showToastMessage(@NonNull CharSequence text);

  @UiThread
  void showToastMessage(@NonNull BaseHttpError error);

  boolean isAdd();

  @UiThread
  void startActivity(Intent intent);

  @UiThread
  void startActivityForResult(@RequiresPermission Intent intent, int requestCode);

  @UiThread
  void startActivityForResult(@RequiresPermission Intent intent, int requestCode,
      @Nullable Bundle options);

  @UiThread
  void thisFinish();
}