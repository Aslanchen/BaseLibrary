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
public abstract class MVPBaseActivity<V extends ViewDataBinding, P extends IMVPBasePresenter> extends
    DataBindBaseActivity<V> {

  protected P mPresenter;

  public abstract P iniPresenter();

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    mPresenter = iniPresenter();
    super.onCreate(savedInstanceState);
  }

  @CallSuper
  @Override
  public void iniBundle(@NonNull Bundle bundle) {
    mPresenter.iniBundle(bundle);
  }

  @CallSuper
  @Override
  public void iniData() {
    mPresenter.onCreate();
  }

  @CallSuper
  @Override
  protected void onResume() {
    mPresenter.onResume();
    super.onResume();
  }

  @CallSuper
  @Override
  protected void onPause() {
    mPresenter.onPause();
    super.onPause();
  }

  @CallSuper
  @Override
  protected void onDestroy() {
    mPresenter.onDestroy();
    super.onDestroy();
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    mPresenter.onActivityResult(requestCode, resultCode, data);
    super.onActivityResult(requestCode, resultCode, data);
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    mPresenter.onRequestPermissionsResult(requestCode, permissions, grantResults);
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
  }
}