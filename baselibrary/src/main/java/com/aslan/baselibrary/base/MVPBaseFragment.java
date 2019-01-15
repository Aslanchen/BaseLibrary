package com.aslan.baselibrary.base;

import android.content.Intent;
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

  public P mPresenter;

  public abstract P iniPresenter();

  @CallSuper
  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    mPresenter = iniPresenter();
    super.onCreate(savedInstanceState);
    getLifecycle().addObserver(mPresenter);
  }

  @CallSuper
  public void iniData() {
    mPresenter.iniData();
  }

  @CallSuper
  @Override
  public void iniBundle(@NonNull Bundle bundle) {
    mPresenter.iniBundle(bundle);
  }

  @CallSuper
  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    mPresenter.onActivityResult(requestCode, resultCode, data);
    super.onActivityResult(requestCode, resultCode, data);
  }

  @CallSuper
  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {
    mPresenter.onRequestPermissionsResult(requestCode, permissions, grantResults);
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
  }
}