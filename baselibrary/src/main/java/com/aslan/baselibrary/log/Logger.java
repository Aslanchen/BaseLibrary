package com.aslan.baselibrary.log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Logger implements Log {

  /**
   * It is used for json pretty print
   */
  private static final int JSON_INDENT = 2;

  private final List<LogAdapter> logAdapters = new ArrayList<>();

  @Override
  public void d(@Nullable String tag, @NonNull String message, @Nullable Object... args) {
    log(android.util.Log.DEBUG, tag, null, message, args);
  }

  @Override
  public void d(@Nullable String tag, @Nullable Object object) {
    log(android.util.Log.DEBUG, tag, null, String.valueOf(object));
  }

  @Override
  public void e(@Nullable String tag, @NonNull String message, @Nullable Object... args) {
    e(tag, null, message, args);
  }

  @Override
  public void e(@Nullable String tag, @Nullable Throwable throwable, @NonNull String message,
      @Nullable Object... args) {
    log(android.util.Log.ERROR, tag, throwable, message, args);
  }

  @Override
  public void w(@Nullable String tag, @NonNull String message, @Nullable Object... args) {
    log(android.util.Log.WARN, tag, null, message, args);
  }

  @Override
  public void i(@Nullable String tag, @NonNull String message, @Nullable Object... args) {
    log(android.util.Log.INFO, tag, null, message, args);
  }

  @Override
  public void v(@Nullable String tag, @NonNull String message, @Nullable Object... args) {
    log(android.util.Log.VERBOSE, tag, null, message, args);
  }

  @Override
  public void wtf(@Nullable String tag, @NonNull String message, @Nullable Object... args) {
    log(android.util.Log.ASSERT, tag, null, message, args);
  }

  @Override
  public void json(@Nullable String tag, @Nullable String json) {
    if (TextUtils.isEmpty(json)) {
      d(tag, "Empty/Null json content");
      return;
    }
    try {
      json = json.trim();
      if (json.startsWith("{")) {
        JSONObject jsonObject = new JSONObject(json);
        String message = jsonObject.toString(JSON_INDENT);
        d(tag, message);
        return;
      }
      if (json.startsWith("[")) {
        JSONArray jsonArray = new JSONArray(json);
        String message = jsonArray.toString(JSON_INDENT);
        d(tag, message);
        return;
      }
      e(tag, "Invalid Json");
    } catch (JSONException e) {
      e(tag, "Invalid Json");
    }
  }

  @Override
  public synchronized void log(int priority,
      @Nullable String tag,
      @Nullable String message,
      @Nullable Throwable throwable) {
    if (TextUtils.isEmpty(message)) {
      message = "Empty/NULL log message";
    }

    for (LogAdapter adapter : logAdapters) {
      if (adapter.isLoggable(priority, tag)) {
        adapter.log(priority, tag, message);
      }
    }
  }

  @Override
  public void clearLogAdapters() {
    logAdapters.clear();
  }

  @Override
  public void addAdapter(@NonNull LogAdapter adapter) {
    logAdapters.add(adapter);
  }

  /**
   * This method is synchronized in order to avoid messy of logs' order.
   */
  private synchronized void log(int priority,
      @Nullable String tag,
      @Nullable Throwable throwable,
      @NonNull String msg,
      @Nullable Object... args) {
    String message = createMessage(msg, args);
    log(priority, tag, message, throwable);
  }

  @NonNull
  private String createMessage(@NonNull String message, @Nullable Object... args) {
    return args == null || args.length == 0 ? message : String.format(message, args);
  }
}
