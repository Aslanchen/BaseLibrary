package com.aslan.baselibrary.executor.callback;

import androidx.lifecycle.Lifecycle.State;
import androidx.lifecycle.LifecycleOwner;
import androidx.annotation.NonNull;
import com.aslan.baselibrary.http.BaseHttpError;
import com.aslan.baselibrary.listener.LoadListCallback;
import java.util.List;

/**
 * 回调和生命周期绑定
 *
 * @author Aslan
 * @date 2019/2/19
 */
public abstract class LifecycleLoadListCallback<T> implements LoadListCallback<T> {

  private LifecycleOwner lifecycleOwner;

  public LifecycleLoadListCallback(LifecycleOwner lifecycleOwner) {
    this.lifecycleOwner = lifecycleOwner;
  }

  @Override
  public void onLoaded(@NonNull List<T> responses) {
    if (lifecycleOwner != null
        && lifecycleOwner.getLifecycle().getCurrentState() == State.DESTROYED) {
      return;
    }
    onLifecycleLoaded(responses);
  }

  @Override
  public void onDataNotAvailable(@NonNull BaseHttpError error) {
    if (lifecycleOwner != null
        && lifecycleOwner.getLifecycle().getCurrentState() == State.DESTROYED) {
      return;
    }
    onLifecycleDataNotAvailable(error);
  }

  public abstract void onLifecycleLoaded(@NonNull List<T> responses);

  public abstract void onLifecycleDataNotAvailable(@NonNull BaseHttpError error);
}
