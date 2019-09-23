package com.aslan.baselibrary.log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * A proxy interface to enable additional operations. Contains all possible Log message usages.
 */
public interface Log {

  void addAdapter(@NonNull LogAdapter adapter);

  void d(@Nullable String tag, @NonNull String message, @Nullable Object... args);

  void d(@Nullable String tag, @Nullable Object object);

  void e(@Nullable String tag, @NonNull String message, @Nullable Object... args);

  void e(@Nullable String tag, @Nullable Throwable throwable, @NonNull String message,
      @Nullable Object... args);

  void w(@Nullable String tag, @NonNull String message, @Nullable Object... args);

  void i(@Nullable String tag, @NonNull String message, @Nullable Object... args);

  void v(@Nullable String tag, @NonNull String message, @Nullable Object... args);

  void wtf(@Nullable String tag, @NonNull String message, @Nullable Object... args);

  /**
   * Formats the given json content and print it
   */
  void json(@Nullable String tag, @Nullable String json);

  void log(int priority, @Nullable String tag, @Nullable String message,
      @Nullable Throwable throwable);

  void clearLogAdapters();
}
