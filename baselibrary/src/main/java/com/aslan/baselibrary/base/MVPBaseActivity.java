package com.aslan.baselibrary.base;

import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.aslan.baselibrary.listener.IMVPBasePresenter;

/**
 * MPV基础类
 *
 * @author Aslan
 * @date 2018/4/11
 */
public abstract class MVPBaseActivity<V extends ViewDataBinding, P extends IMVPBasePresenter> extends
    DataBindBaseActivity<V> {

  protected P mPresentrt;

  public abstract P iniPresenter();

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    mPresentrt = iniPresenter();
    super.onCreate(savedInstanceState);
  }

  @CallSuper
  @Override
  public void iniBundle(@NonNull Bundle bundle) {
    mPresentrt.iniBundle(bundle);
  }

  @CallSuper
  @Override
  public void iniData() {
    mPresentrt.onCreate();
  }

  @CallSuper
  @Override
  protected void onResume() {
    mPresentrt.onResume();
    super.onResume();
  }

  @CallSuper
  @Override
  protected void onPause() {
    mPresentrt.onPause();
    super.onPause();
  }

  @CallSuper
  @Override
  protected void onDestroy() {
    mPresentrt.onDestroy();
    super.onDestroy();
  }
}