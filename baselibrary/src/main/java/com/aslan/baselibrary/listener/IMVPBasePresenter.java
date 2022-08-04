package com.aslan.baselibrary.listener;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.DefaultLifecycleObserver;

/**
 * MPV基础类
 *
 * @author Aslan
 * @date 2018/4/11
 */
public interface IMVPBasePresenter extends DefaultLifecycleObserver {

  void iniBundle(@NonNull Bundle bundle);

  void iniData();

  void onActivityResult(int requestCode, int resultCode, @Nullable Intent data);

  void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults);

  @Nullable
  FragmentManager getFragmentManager();
}