package com.aslan.baselibrary.listener;

import android.os.Bundle;
import android.support.annotation.NonNull;

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
}