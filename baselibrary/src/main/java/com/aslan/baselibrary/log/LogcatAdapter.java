package com.aslan.baselibrary.log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.Log;

/**
 * AndroidLogcat
 */
public class LogcatAdapter implements LogAdapter {

  @Override
  public boolean isLoggable(int priority, @Nullable String tag) {
    return true;
  }

  @Override
  public void log(int priority, @Nullable String tag, @NonNull String message) {
    Log.println(priority, tag, message);
  }
}