package com.aslan.baselibrary.base;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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