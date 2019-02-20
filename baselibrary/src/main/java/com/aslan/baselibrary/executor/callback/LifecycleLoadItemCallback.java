package com.aslan.baselibrary.executor.callback;

import android.arch.lifecycle.Lifecycle.State;
import android.arch.lifecycle.LifecycleOwner;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.aslan.baselibrary.http.BaseHttpError;
import com.aslan.baselibrary.listener.LoadItemCallback;

/**
 * 回调和生命周期绑定
 *
 * @author Aslan
 * @date 2019/2/19
 */
public abstract class LifecycleLoadItemCallback<T> implements LoadItemCallback<T> {

  private LifecycleOwner lifecycleOwner;

  public LifecycleLoadItemCallback(LifecycleOwner lifecycleOwner) {
    this.lifecycleOwner = lifecycleOwner;
  }

  @Override
  public void onLoaded(@Nullable T respone) {
    if (lifecycleOwner != null
        && lifecycleOwner.getLifecycle().getCurrentState() == State.DESTROYED) {
      return;
    }
    onLifecycleLoaded(respone);
  }

  @Override
  public void onDataNotAvailable(@NonNull BaseHttpError error) {
    if (lifecycleOwner != null
        && lifecycleOwner.getLifecycle().getCurrentState() == State.DESTROYED) {
      return;
    }
    onLifecycleDataNotAvailable(error);
  }

  public abstract void onLifecycleLoaded(@Nullable T response);

  public abstract void onLifecycleDataNotAvailable(@NonNull BaseHttpError error);
}
