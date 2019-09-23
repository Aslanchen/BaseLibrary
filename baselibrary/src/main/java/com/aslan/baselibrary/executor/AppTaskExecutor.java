package com.aslan.baselibrary.executor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 多线程管理类
 *
 * @author Aslanchen
 * @date 2018/05/15
 */
public class AppTaskExecutor extends TaskExecutor {

  private static volatile AppTaskExecutor sInstance;

  @NonNull
  private TaskExecutor mDelegate;

  @NonNull
  private TaskExecutor mDefaultTaskExecutor;

  private AppTaskExecutor() {
    mDefaultTaskExecutor = new DefaultTaskExecutor();
    mDelegate = mDefaultTaskExecutor;
  }

  /**
   * Returns an instance of the task executor.
   *
   * @return The singleton ArchTaskExecutor.
   */
  @NonNull
  public static AppTaskExecutor getInstance() {
    if (sInstance != null) {
      return sInstance;
    }
    synchronized (AppTaskExecutor.class) {
      if (sInstance == null) {
        sInstance = new AppTaskExecutor();
      }
    }
    return sInstance;
  }

  /**
   * Sets a delegate to handle task execution requests. <p> If you have a common executor, you can
   * set it as the delegate and App Toolkit components will use your executors. You may also want to
   * use this for your tests. <p> Calling this method with {@code null} sets it to the default
   * TaskExecutor.
   *
   * @param taskExecutor The task executor to handle task requests.
   */
  public void setDelegate(@Nullable TaskExecutor taskExecutor) {
    mDelegate = taskExecutor == null ? mDefaultTaskExecutor : taskExecutor;
  }

  @Override
  public void executeOnDiskIO(@NonNull Runnable runnable) {
    mDelegate.executeOnDiskIO(runnable);
  }

  @Override
  public void executeOnNetwork(@NonNull Runnable runnable) {
    mDelegate.executeOnNetwork(runnable);
  }

  @Override
  void postToMainThread(@NonNull Runnable runnable) {
    mDelegate.postToMainThread(runnable);
  }

  @Override
  public void postToMainThreadDelayed(@NonNull Runnable runnable, long delayMillis) {
    mDelegate.postToMainThreadDelayed(runnable, delayMillis);
  }

  @Override
  public void removeRunable(@NonNull Runnable runnable) {
    mDelegate.removeRunable(runnable);
  }

  @Override
  public boolean isMainThread() {
    return mDelegate.isMainThread();
  }
}

