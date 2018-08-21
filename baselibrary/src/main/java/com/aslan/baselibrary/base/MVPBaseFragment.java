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
public abstract class MVPBaseFragment<V extends ViewDataBinding, P extends IMVPBasePresenter> extends
    DataBindBaseFragment<V> {

  public P mPresentrt;

  public abstract P iniPresenter();

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
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
  public void onResume() {
    mPresentrt.onResume();
    super.onResume();
  }

  @CallSuper
  @Override
  public void onPause() {
    mPresentrt.onPause();
    super.onPause();
  }

  @CallSuper
  @Override
  public void onDestroy() {
    mPresentrt.onDestroy();
    super.onDestroy();
  }
}