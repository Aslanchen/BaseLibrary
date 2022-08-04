package com.aslan.baselibrary.base;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import com.aslan.baselibrary.listener.IMVPBasePresenter;

/**
 * MPV基础类
 *
 * @author Aslan
 * @date 2018/4/11
 */
public abstract class MVPBaseActivity<VB extends ViewBinding, P extends IMVPBasePresenter> extends
    VBBaseActivity<VB> {

  protected P mPresenter;

  public abstract P iniPresenter();

  @CallSuper
  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
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
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
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