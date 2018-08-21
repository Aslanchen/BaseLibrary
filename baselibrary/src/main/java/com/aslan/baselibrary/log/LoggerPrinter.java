package com.aslan.baselibrary.log;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoggerPrinter implements Printer {

  /**
   * It is used for json pretty print
   */
  private static final int JSON_INDENT = 2;

  /**
   * Provides one-time used tag for the log message
   */
  private final ThreadLocal<String> localTag = new ThreadLocal<>();

  private final List<LogAdapter> logAdapters = new ArrayList<>();

  @Override
  public Printer t(String tag) {
    if (tag != null) {
      localTag.set(tag);
    }
    return this;
  }

  @Override
  public void d(@NonNull String message, @Nullable Object... args) {
    log(Log.DEBUG, null, message, args);
  }

  @Override
  public void d(@Nullable Object object) {
    log(Log.DEBUG, null, String.valueOf(object));
  }

  @Override
  public void e(@NonNull String message, @Nullable Object... args) {
    e(null, message, args);
  }

  @Override
  public void e(@Nullable Throwable throwable, @NonNull String message, @Nullable Object... args) {
    log(Log.ERROR, throwable, message, args);
  }

  @Override
  public void w(@NonNull String message, @Nullable Object... args) {
    log(Log.WARN, null, message, args);
  }

  @Override
  public void i(@NonNull String message, @Nullable Object... args) {
    log(Log.INFO, null, message, args);
  }

  @Override
  public void v(@NonNull String message, @Nullable Object... args) {
    log(Log.VERBOSE, null, message, args);
  }

  @Override
  public void wtf(@NonNull String message, @Nullable Object... args) {
    log(Log.ASSERT, null, message, args);
  }

  @Override
  public void json(@Nullable String json) {
    if (TextUtils.isEmpty(json)) {
      d("Empty/Null json content");
      return;
    }
    try {
      json = json.trim();
      if (json.startsWith("{")) {
        JSONObject jsonObject = new JSONObject(json);
        String message = jsonObject.toString(JSON_INDENT);
        d(message);
        return;
      }
      if (json.startsWith("[")) {
        JSONArray jsonArray = new JSONArray(json);
        String message = jsonArray.toString(JSON_INDENT);
        d(message);
        return;
      }
      e("Invalid Json");
    } catch (JSONException e) {
      e("Invalid Json");
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
      @Nullable Throwable throwable,
      @NonNull String msg,
      @Nullable Object... args) {
    String tag = getTag();
    String message = createMessage(msg, args);
    log(priority, tag, message, throwable);
  }

  /**
   * @return the appropriate tag based on local or global
   */
  @Nullable
  private String getTag() {
    String tag = localTag.get();
    if (tag != null) {
      localTag.remove();
      return tag;
    }
    return null;
  }

  @NonNull
  private String createMessage(@NonNull String message, @Nullable Object... args) {
    return args == null || args.length == 0 ? message : String.format(message, args);
  }
}
