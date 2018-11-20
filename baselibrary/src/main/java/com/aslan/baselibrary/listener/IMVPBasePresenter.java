package com.aslan.baselibrary.listener;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * MPV基础类
 *
 * @author Aslan
 * @date 2018/4/11
 */
public interface IMVPBasePresenter {

  void iniBundle(@NonNull Bundle bundle);

  void onCreate();

  void onResume();

  void onPause();

  void onDestroy();

  void onActivityResult(int requestCode, int resultCode, @Nullable Intent data);

  void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults);
}