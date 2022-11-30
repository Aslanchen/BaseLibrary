package com.aslan.baselibrary.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;
import com.aslan.baselibrary.R;

/**
 * 头部toolbar
 *
 * @author Aslan
 * @date 2018/4/11
 */
public class CustomToolbar extends Toolbar {

  private int mTitleTextAppearance;
  private TextView mTitleTextView;
  private CharSequence mTitleText;

  private Drawable iconBack;

  private ColorStateList mTitleTextColor;

  private int mGravity = Gravity.CENTER;

  public CustomToolbar(Context context) {
    this(context, null);
  }

  public CustomToolbar(Context context, @Nullable AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public CustomToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);

    TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomToolbar);
    mTitleTextAppearance = typedArray.getResourceId(
        R.styleable.CustomToolbar_ct_titleTextAppearance, 0);
    mGravity = typedArray.getInt(R.styleable.CustomToolbar_ct_gravity, Gravity.CENTER);

    CharSequence title = typedArray.getText(R.styleable.CustomToolbar_ct_title);
    setTitle(title);

    iconBack = typedArray.getDrawable(R.styleable.CustomToolbar_ct_icon_back);
    boolean showBack = typedArray.getBoolean(R.styleable.CustomToolbar_ct_showback, true);
    setShowBack(showBack);

    if (typedArray.hasValue(R.styleable.CustomToolbar_ct_titleTextColor)) {
      setTitleTextColor(typedArray.getColorStateList(R.styleable.CustomToolbar_ct_titleTextColor));
    }
    typedArray.recycle();
  }

  @Override
  public void setTitle(@StringRes int resId) {
    setTitle(getResources().getString(resId));
  }

  @Override
  public void setTitle(CharSequence title) {
    if (!TextUtils.isEmpty(title)) {
      if (mTitleTextView == null) {
        mTitleTextView = new AppCompatTextView(getContext());
        mTitleTextView.setSingleLine();
        mTitleTextView.setEllipsize(TextUtils.TruncateAt.END);
        if (mTitleTextAppearance != 0) {
          mTitleTextView.setTextAppearance(getContext(), mTitleTextAppearance);
        }

        if (mTitleTextColor != null) {
          mTitleTextView.setTextColor(mTitleTextColor);
        }
      }

      if (indexOfChild(mTitleTextView) < 0) {
        addSystemView(mTitleTextView);
      }
    } else {
      if (mTitleTextView != null) {
        removeView(mTitleTextView);
      }
    }

    if (mTitleTextView != null) {
      mTitleTextView.setText(title);
    }
    mTitleText = title;
  }

  @Override
  public CharSequence getTitle() {
    return mTitleText;
  }

  private void addSystemView(View v) {
    final ViewGroup.LayoutParams vlp = v.getLayoutParams();
    final LayoutParams lp;
    if (vlp == null) {
      lp = generateDefaultLayoutParams();
    } else if (!checkLayoutParams(vlp)) {
      lp = generateLayoutParams(vlp);
    } else {
      lp = (LayoutParams) vlp;
    }

    lp.gravity = mGravity;
    addView(v, lp);
  }

  protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
    if (p instanceof LayoutParams) {
      return new LayoutParams((LayoutParams) p);
    } else if (p instanceof ActionBar.LayoutParams) {
      return new LayoutParams((ActionBar.LayoutParams) p);
    } else if (p instanceof MarginLayoutParams) {
      return new LayoutParams((MarginLayoutParams) p);
    } else {
      return new LayoutParams(p);
    }
  }

  public void setTitleTextColor(@ColorInt int color) {
    setTitleTextColor(ColorStateList.valueOf(color));
  }

  public void setTitleTextColor(@NonNull ColorStateList color) {
    mTitleTextColor = color;
    if (mTitleTextView != null) {
      mTitleTextView.setTextColor(color);
    }
  }

  public void setBackIcon(@DrawableRes int resId) {
    iconBack = AppCompatResources.getDrawable(getContext(), resId);
    setNavigationIcon(iconBack);
  }

  public void setGravity(int gravity) {
    Toolbar.LayoutParams vlp = (LayoutParams) mTitleTextView.getLayoutParams();
    vlp.gravity = gravity;
  }

  public void setShowBack(boolean isShow) {
    if (isShow) {
      if (iconBack == null) {
        Theme theme = getContext().getTheme();
        iconBack = VectorDrawableCompat.create(getResources(), R.drawable.icon_back, theme);
      }
      setNavigationIcon(iconBack);
    } else {
      setNavigationIcon(null);
    }
  }
}
