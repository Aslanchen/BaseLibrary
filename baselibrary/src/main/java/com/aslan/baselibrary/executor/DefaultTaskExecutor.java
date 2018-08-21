package com.aslan.baselibrary.executor;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 多线程管理类
 *
 * @author Aslanchen
 * @date 2018/05/15
 */
public class DefaultTaskExecutor extends TaskExecutor {

  private final Object mLock = new Object();

  private ExecutorService mDiskIO = Executors.newSingleThreadExecutor();
  private ExecutorService mNetworkIO = Executors.newFixedThreadPool(3);

  @Nullable
  private volatile Handler mMainHandler;

  @Override
  public void executeOnDiskIO(@NonNull Runnable runnable) {
    mDiskIO.execute(runnable);
  }

  @Override
  public void executeOnNetwork(@NonNull Runnable runnable) {
    mNetworkIO.execute(runnable);
  }

  @Override
  void postToMainThread(@NonNull Runnable runnable) {
    getMain();
    //noinspection ConstantConditions
    mMainHandler.post(runnable);
  }

  @Override
  public void postToMainThreadDelayed(@NonNull Runnable runnable, long delayMillis) {
    getMain();
    //noinspection ConstantConditions
    mMainHandler.postDelayed(runnable, delayMillis);
  }

  @Override
  public void removeRunable(@NonNull Runnable runnable) {
    if (mMainHandler != null) {
      mMainHandler.removeCallbacks(runnable);
    }
  }

  private void getMain() {
    if (mMainHandler == null) {
      synchronized (mLock) {
        if (mMainHandler == null) {
          mMainHandler = new Handler(Looper.getMainLooper());
        }
      }
    }
  }

  @Override
  public boolean isMainThread() {
    return Looper.getMainLooper().getThread() == Thread.currentThread();
  }
}