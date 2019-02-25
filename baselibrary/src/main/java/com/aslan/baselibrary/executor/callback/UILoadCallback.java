package com.aslan.baselibrary.executor.callback;

import android.arch.lifecycle.LifecycleOwner;
import android.support.annotation.NonNull;
import com.aslan.baselibrary.executor.AppTaskExecutor;
import com.aslan.baselibrary.http.BaseHttpError;

/**
 * 回调放入主线程处理
 *
 * @author Aslan
 * @date 2019/2/19
 */
public abstract class UILoadCallback extends LifecycleLoadCallback {

  public UILoadCallback(LifecycleOwner lifecycleOwner) {
    super(lifecycleOwner);
  }

  @Override
  public void onLifecycleLoaded() {
    if (AppTaskExecutor.getInstance().isMainThread()) {
      onUILoaded();
    } else {
      AppTaskExecutor.getInstance().executeOnMainThread(new Runnable() {
        @Override
        public void run() {
          onUILoaded();
        }
      });
    }
  }

  @Override
  public void onLifecycleDataNotAvailable(@NonNull BaseHttpError error) {
    if (AppTaskExecutor.getInstance().isMainThread()) {
      onUIDataNotAvailable(error);
    } else {
      AppTaskExecutor.getInstance().executeOnMainThread(new Runnable() {
        @Override
        public void run() {
          onUIDataNotAvailable(error);
        }
      });
    }
  }

  public abstract void onUILoaded();

  public abstract void onUIDataNotAvailable(@NonNull BaseHttpError error);
}
