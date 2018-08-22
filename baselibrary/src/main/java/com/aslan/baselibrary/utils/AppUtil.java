package com.aslan.baselibrary.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import java.util.UUID;

/**
 * 通用操作类
 *
 * @author Aslan
 * @date 2018/4/11
 */
public final class AppUtil {

  private static PackageInfo packageInfo;

  public static int CompoundDrawableLeft = 1;
  public static int CompoundDrawableTop = 2;
  public static int CompoundDrawableRight = 3;
  public static int CompoundDrawableBottom = 4;

  public static void setCompoundDrawables(TextView textView, @DrawableRes int id, int direction) {
    Drawable drawable = textView.getResources().getDrawable(id);
    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());

    if (direction == CompoundDrawableLeft) {
      textView.setCompoundDrawables(drawable, null, null, null);
    } else if (direction == CompoundDrawableTop) {
      textView.setCompoundDrawables(null, drawable, null, null);
    } else if (direction == CompoundDrawableRight) {
      textView.setCompoundDrawables(null, null, drawable, null);
    } else if (direction == CompoundDrawableBottom) {
      textView.setCompoundDrawables(null, null, null, drawable);
    }
  }

  public static void clearCompoundDrawables(TextView textView) {
    textView.setCompoundDrawables(null, null, null, null);
  }

  /**
   * 隐藏输入法
   */
  public static void hideInputSoft(Context context, View view) {
    InputMethodManager manager = (InputMethodManager) context
        .getSystemService(Context.INPUT_METHOD_SERVICE);
    if (manager == null) {
      return;
    }
    manager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
  }

  /**
   * 显示键盘
   */
  public static void showInputMethod(Context context, View view) {
    InputMethodManager manager = (InputMethodManager) context
        .getSystemService(Context.INPUT_METHOD_SERVICE);
    if (manager == null) {
      return;
    }
    manager.showSoftInput(view, 0);
  }

  /**
   * 显示键盘
   */
  public static void showInputMethod(Context context) {
    InputMethodManager manager = (InputMethodManager) context
        .getSystemService(Context.INPUT_METHOD_SERVICE);
    if (manager == null) {
      return;
    }
    manager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
  }

  /**
   * 将sp值转换为px值，保证文字大小不变
   *
   * @param spValue （DisplayMetrics类中属性scaledDensity）
   */
  public static int sp2px(Context context, float spValue) {
    final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
    return (int) (spValue * fontScale + 0.5f);
  }

  /**
   * dip转为 px
   */
  public static int dip2px(Context context, float dipValue) {
    final float scale = context.getResources().getDisplayMetrics().density;
    return (int) (dipValue * scale + 0.5f);
  }

  public static String getUUID() {
    return UUID.randomUUID().toString();
  }

  /**
   * 获取当前应用的版本号
   */
  public static PackageInfo getPackageInfo(Context context) {
    if (packageInfo != null) {
      return packageInfo;
    }

    // 获取packagemanager的实例
    PackageManager packageManager = context.getApplicationContext().getPackageManager();
    // getPackageName()是你当前类的包名，0代表是获取版本信息
    PackageInfo packInfo = null;
    try {
      packInfo = packageManager
          .getPackageInfo(context.getApplicationContext().getPackageName(), 0);
      AppUtil.packageInfo = packInfo;
      return AppUtil.packageInfo;
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
    }
    return null;
  }
}
