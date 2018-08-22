package com.aslan.baselibrary.utils;

import android.graphics.drawable.GradientDrawable;
import android.support.annotation.ColorInt;
import android.view.View;

/**
 * 自定义圆角
 *
 * @author Aslanchen
 * @date 2018/05/15
 */

public class GradientDrawableBuild {

  private GradientDrawable gradientDrawable;

  public GradientDrawableBuild() {
    gradientDrawable = new GradientDrawable();
  }

  public static GradientDrawableBuild Creat() {
    return new GradientDrawableBuild();
  }

  public GradientDrawableBuild setColor(@ColorInt int argb) {
    gradientDrawable.setColor(argb);
    return this;
  }

  public GradientDrawableBuild setStroke(int width, @ColorInt int color) {
    gradientDrawable.setStroke(width, color);
    return this;
  }

  public GradientDrawableBuild setCornerRadii(float left, float top, float right, float bottom) {
    gradientDrawable
        .setCornerRadii(new float[]{left, left, top, top, right, right, bottom, bottom});
    return this;
  }

  public GradientDrawableBuild setCornerRadius(float radius) {
    gradientDrawable.setCornerRadius(radius);
    return this;
  }

  public GradientDrawable Build() {
    return gradientDrawable;
  }

  public void setBackground(View view) {
    view.setBackground(gradientDrawable);
  }
}
