package com.aslan.baselibrary.base;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

/**
 * 基础类
 *
 * @author Aslan
 * @date 2018/4/11
 */
public abstract class DataBindBaseFragment<V extends ViewDataBinding> extends BaseFragment {

  protected V mDataBinding;

  @Override
  public View setCusContentView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
    mDataBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
    return mDataBinding.getRoot();
  }

  @Override
  public final void iniView(View view) {
    iniView();
  }

  public abstract void iniView();
}