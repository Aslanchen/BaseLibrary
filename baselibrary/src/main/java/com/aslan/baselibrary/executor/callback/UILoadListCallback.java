package com.aslan.baselibrary.executor.callback;

import androidx.lifecycle.LifecycleOwner;
import androidx.annotation.NonNull;
import com.aslan.baselibrary.executor.AppTaskExecutor;
import com.aslan.baselibrary.http.BaseHttpError;
import java.util.List;

/**
 * 回调放入主线程处理
 *
 * @author Aslan
 * @date 2019/2/19
 */
public abstract class UILoadListCallback<T> extends LifecycleLoadListCallback<T> {

  public UILoadListCallback(LifecycleOwner lifecycleOwner) {
    super(lifecycleOwner);
  }

  @Override
  public void onLifecycleLoaded(@NonNull List<T> responses) {
    if (AppTaskExecutor.getInstance().isMainThread()) {
      onUILoaded(responses);
    } else {
      AppTaskExecutor.getInstance().executeOnMainThread(new Runnable() {
        @Override
        public void run() {
          onUILoaded(responses);
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

  public abstract void onUILoaded(@NonNull List<T> responses);

  public abstract void onUIDataNotAvailable(@NonNull BaseHttpError error);
}
