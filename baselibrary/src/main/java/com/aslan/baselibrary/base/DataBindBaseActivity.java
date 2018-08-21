package com.aslan.baselibrary.base;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import com.umeng.analytics.MobclickAgent;

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

  @Override
  protected void onResume() {
    super.onResume();
    MobclickAgent.onResume(this);
  }

  @Override
  protected void onPause() {
    super.onPause();
    MobclickAgent.onPause(this);
  }
}