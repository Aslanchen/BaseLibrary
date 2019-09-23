package com.aslan.baselibrary.base;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

/**
 * 基础类
 *
 * @author Aslan
 * @date 2018/4/11
 */
public abstract class DataBindBaseActivity<V extends ViewDataBinding> extends BaseActivity {

  protected V mDataBinding;

  @Override
  public void setCusContentView() {
    mDataBinding = DataBindingUtil.setContentView(this, getLayoutId());
  }
}