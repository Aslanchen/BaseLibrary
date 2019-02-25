package com.aslan.baselibrary.executor.callback;

import android.arch.lifecycle.Lifecycle.State;
import android.arch.lifecycle.LifecycleOwner;
import android.support.annotation.NonNull;
import com.aslan.baselibrary.http.BaseHttpError;
import com.aslan.baselibrary.listener.LoadCallback;

/**
 * 回调和生命周期绑定
 *
 * @author Aslan
 * @date 2019/2/19
 */
public abstract class LifecycleLoadCallback implements LoadCallback {

  private LifecycleOwner lifecycleOwner;

  public LifecycleLoadCallback(LifecycleOwner lifecycleOwner) {
    this.lifecycleOwner = lifecycleOwner;
  }

  @Override
  public void onLoaded() {
    if (lifecycleOwner != null
        && lifecycleOwner.getLifecycle().getCurrentState() == State.DESTROYED) {
      return;
    }
    onLifecycleLoaded();
  }

  @Override
  public void onDataNotAvailable(@NonNull BaseHttpError error) {
    if (lifecycleOwner != null
        && lifecycleOwner.getLifecycle().getCurrentState() == State.DESTROYED) {
      return;
    }
    onLifecycleDataNotAvailable(error);
  }

  public abstract void onLifecycleLoaded();

  public abstract void onLifecycleDataNotAvailable(@NonNull BaseHttpError error);
}
