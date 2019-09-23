package com.aslan.baselibrary.executor;

import androidx.annotation.NonNull;

/**
 * 多线程管理类
 *
 * @author Aslanchen
 * @date 2018/05/15
 */
public abstract class TaskExecutor {

  /**
   * Executes the given task in the disk IO thread pool.
   */
  public abstract void executeOnDiskIO(@NonNull Runnable runnable);


  /**
   * Executes the given task in the Net thread pool.
   */
  public abstract void executeOnNetwork(@NonNull Runnable runnable);

  /**
   * Posts the given task to the main thread.
   */
  abstract void postToMainThread(@NonNull Runnable runnable);

  public abstract void postToMainThreadDelayed(@NonNull Runnable runnable, long delayMillis);

  /**
   * Executes the given task on the main thread. <p> If the current thread is a main thread,
   * immediately runs the given runnable.
   */
  public void executeOnMainThread(@NonNull Runnable runnable) {
    if (isMainThread()) {
      runnable.run();
    } else {
      postToMainThread(runnable);
    }
  }

  public abstract void removeRunable(Runnable runnable);

  /**
   * Returns true if the current thread is the main thread, false otherwise.
   *
   * @return true if we are on the main thread, false otherwise.
   */
  public abstract boolean isMainThread();
}
