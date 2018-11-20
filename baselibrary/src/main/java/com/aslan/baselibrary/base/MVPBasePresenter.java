package com.aslan.baselibrary.base;

import android.arch.lifecycle.LifecycleOwner;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import com.aslan.baselibrary.listener.IBaseView;
import com.aslan.baselibrary.listener.IMVPBasePresenter;

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
  public FragmentManager fragmentManager;

  @SuppressWarnings({"unchecked"})
  public MVPBasePresenter(BaseActivity activity) {
    this.context = activity;
    this.lifecycleOwner = activity;
    this.mView = (V) activity;
    this.activity = activity;
    this.fragmentManager = activity.getSupportFragmentManager();
  }

  @SuppressWarnings({"unchecked"})
  public MVPBasePresenter(BaseFragment fragment) {
    this.context = fragment.getContext();
    this.lifecycleOwner = fragment;
    this.mView = (V) fragment;
    this.fragment = fragment;
    this.fragmentManager = fragment.getFragmentManager();
  }

  @Override
  public void onResume() {

  }

  @Override
  public void onPause() {

  }

  @Override
  public void onDestroy() {

  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

  }
}