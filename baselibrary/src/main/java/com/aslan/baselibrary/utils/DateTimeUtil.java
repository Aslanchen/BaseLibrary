package com.aslan.baselibrary.utils;

/**
 * 日期时间操作类
 *
 * @author Aslan
 * @date 2018/4/11
 */
public final class DateTimeUtil {

  public static final long SECOND = 1000;
  public static final long MINUTE = SECOND * 60;
  public static final long HOURS = MINUTE * 60;
  public static final long DAY = HOURS * 24;

  /**
   * 时间差
   */
  private static long timeoffset = 0;

  private DateTimeUtil() {

  }

  public static long currentTimeMillis() {
    return System.currentTimeMillis() + timeoffset;
  }
}
