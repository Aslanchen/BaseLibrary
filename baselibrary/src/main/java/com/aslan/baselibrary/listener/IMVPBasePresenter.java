package com.aslan.baselibrary.listener;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;

/**
 * MPV基础类
 *
 * @author Aslan
 * @date 2018/4/11
 */
public interface IMVPBasePresenter extends LifecycleObserver {

  void iniBundle(@NonNull Bundle bundle);

  @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
  void onCreate();

  void iniData();

  @OnLifecycleEvent(Lifecycle.Event.ON_START)
  void onStart();

  @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
  void onResume();

  @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
  void onPause();

  @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
  void onStop();

  @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
  void onDestroy();

  void onActivityResult(int requestCode, int resultCode, @Nullable Intent data);

  void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults);

  @Nullable
  FragmentManager getFragmentManager();
}