package com.aslan.baselibrary.base;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import com.aslan.baselibrary.listener.IBaseView;
import com.aslan.baselibrary.listener.IMVPBasePresenter;
import pub.devrel.easypermissions.EasyPermissions;
import pub.devrel.easypermissions.EasyPermissions.PermissionCallbacks;

/**
 * MPV基础类
 *
 * @author Aslan
 * @date 2018/4/11
 */
public abstract class MVPBasePresenter<V extends IBaseView> implements IMVPBasePresenter {

  public V mView;
  public Context context;
  public LifecycleOwner lifecycleOwner;
  public BaseActivity activity;
  public BaseFragment fragment;

  @SuppressWarnings({"unchecked"})
  public MVPBasePresenter(BaseActivity activity) {
    this.context = activity;
    this.lifecycleOwner = activity;
    this.mView = (V) activity;
    this.activity = activity;
  }

  @SuppressWarnings({"unchecked"})
  public MVPBasePresenter(BaseFragment fragment) {
    this.context = fragment.getContext();
    this.lifecycleOwner = fragment;
    this.mView = (V) fragment;
    this.fragment = fragment;
    this.activity = (BaseActivity) fragment.getActivity();
  }

  @Override
  public void iniBundle(@NonNull Bundle bundle) {

  }

  @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
  @Override
  public void onCreate() {
  }

  @Override
  public void iniData() {
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_START)
  @Override
  public void onStart() {
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
  @Override
  public void onResume() {
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
  @Override
  public void onPause() {
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
  @Override
  public void onStop() {
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
  @Override
  public void onDestroy() {
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {
    if (this instanceof PermissionCallbacks) {
      EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
  }

  @Nullable
  @Override
  public FragmentManager getFragmentManager() {
    if (fragment != null) {
      return fragment.getFragmentManager();
    }

    return activity.getSupportFragmentManager();
  }
}