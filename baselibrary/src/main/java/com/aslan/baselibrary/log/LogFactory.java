package com.aslan.baselibrary.log;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * 日志类
 *
 * @author Aslanchen
 * @date 2018/05/15
 */
public final class LogFactory {

  private static Log log = new Logger();

  private LogFactory() {
    //no instance
  }

  public static void printer(@NonNull Log log) {
    LogFactory.log = log;
  }

  public static void addLogAdapter(@NonNull LogAdapter adapter) {
    log.addAdapter(adapter);
  }

  public static void clearLogAdapters() {
    log.clearLogAdapters();
  }

  /**
   * General log function that accepts all configurations as parameter
   */
  public static void log(int priority, @Nullable String tag, @Nullable String message,
      @Nullable Throwable throwable) {
    log.log(priority, tag, message, throwable);
  }

  public static void d(@Nullable String tag, @NonNull String message, @Nullable Object... args) {
    log.d(tag, message, args);
  }

  public static void d(@Nullable String tag, @Nullable Object object) {
    log.d(tag, object);
  }

  public static void e(@Nullable String tag, @NonNull String message, @Nullable Object... args) {
    log.e(tag, null, message, args);
  }

  public static void e(@Nullable String tag, @Nullable Throwable throwable, @NonNull String message,
      @Nullable Object... args) {
    log.e(tag, throwable, message, args);
  }

  public static void i(@Nullable String tag, @NonNull String message, @Nullable Object... args) {
    log.i(tag, message, args);
  }

  public static void v(@Nullable String tag, @NonNull String message, @Nullable Object... args) {
    log.v(tag, message, args);
  }

  public static void w(@Nullable String tag, @NonNull String message, @Nullable Object... args) {
    log.w(tag, message, args);
  }

  /**
   * Tip: Use this for exceptional situations to log ie: Unexpected errors etc
   */
  public static void wtf(@Nullable String tag, @NonNull String message, @Nullable Object... args) {
    log.wtf(tag, message, args);
  }

  /**
   * Formats the given json content and print it
   */
  public static void json(@Nullable String tag, @Nullable String json) {
    log.json(tag, json);
  }
}
