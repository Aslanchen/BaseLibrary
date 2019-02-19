package com.aslan.baselibrary.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SharePreference管理类
 *
 * @author Aslanchen
 * @date 2018/05/15
 */
public class SharePreferenceUtils {

  public final static String SHARED_PREFERENCES_NAME = "BaseSharePreference";

  /**
   * 向SharedPreferences中写入int类型数据
   *
   * @param context 上下文环境
   * @param key 键
   * @param value 值
   */
  public static void putValue(Context context, String key, int value) {
    SharedPreferences.Editor sp = getEditor(context);
    sp.putInt(key, value);
    sp.apply();
  }

  /**
   * 向SharedPreferences中写入boolean类型的数据
   *
   * @param context 上下文环境
   * @param key 键
   * @param value 值
   */
  public static void putValue(Context context, String key, boolean value) {
    SharedPreferences.Editor sp = getEditor(context);
    sp.putBoolean(key, value);
    sp.apply();
  }

  /**
   * 向SharedPreferences中写入String类型的数据
   *
   * @param context 上下文环境
   * @param key 键
   * @param value 值
   */
  public static void putValue(Context context, String key, String value) {
    SharedPreferences.Editor sp = getEditor(context);
    sp.putString(key, value);
    sp.apply();
  }

  /**
   * 向SharedPreferences中写入float类型的数据
   *
   * @param context 上下文环境
   * @param key 键
   * @param value 值
   */
  public static void putValue(Context context, String key, float value) {
    SharedPreferences.Editor sp = getEditor(context);
    sp.putFloat(key, value);
    sp.apply();
  }

  /**
   * 向SharedPreferences中写入long类型的数据
   *
   * @param context 上下文环境
   * @param key 键
   * @param value 值
   */
  public static void putValue(Context context, String key, long value) {
    SharedPreferences.Editor sp = getEditor(context);
    sp.putLong(key, value);
    sp.apply();
  }

  /**
   * 从SharedPreferences中读取int类型的数据
   *
   * @param context 上下文环境
   * @param key 键
   * @param defValue 如果读取不成功则使用默认值
   * @return 返回读取的值
   */
  public static int getValue(Context context, String key, int defValue) {
    SharedPreferences sp = getSharedPreferences(context);
    int value = sp.getInt(key, defValue);
    return value;
  }

  /**
   * 从SharedPreferences中读取boolean类型的数据
   *
   * @param context 上下文环境
   * @param key 键
   * @param defValue 如果读取不成功则使用默认值
   * @return 返回读取的值
   */
  public static boolean getValue(Context context, String key, boolean defValue) {
    SharedPreferences sp = getSharedPreferences(context);
    boolean value = sp.getBoolean(key, defValue);
    return value;
  }

  /**
   * 从SharedPreferences中读取String类型的数据
   *
   * @param context 上下文环境
   * @param key 键
   * @param defValue 如果读取不成功则使用默认值
   * @return 返回读取的值
   */
  public static String getValue(Context context, String key, String defValue) {
    SharedPreferences sp = getSharedPreferences(context);
    String value = sp.getString(key, defValue);
    return value;
  }

  /**
   * 从SharedPreferences中读取float类型的数据
   *
   * @param context 上下文环境
   * @param key 键
   * @param defValue 如果读取不成功则使用默认值
   * @return 返回读取的值
   */
  public static float getValue(Context context, String key, float defValue) {
    SharedPreferences sp = getSharedPreferences(context);
    float value = sp.getFloat(key, defValue);
    return value;
  }

  /**
   * 从SharedPreferences中读取long类型的数据
   *
   * @param context 上下文环境
   * @param key 键
   * @param defValue 如果读取不成功则使用默认值
   * @return 返回读取的值
   */
  public static long getValue(Context context, String key, long defValue) {
    SharedPreferences sp = getSharedPreferences(context);
    long value = sp.getLong(key, defValue);
    return value;
  }

  /**
   * 移除
   */
  public static void remove(Context context, String key) {
    SharedPreferences.Editor editor = getEditor(context);
    editor.remove(key);
    editor.apply();
  }

  //获取Editor实例
  public static SharedPreferences.Editor getEditor(Context context) {
    return getSharedPreferences(context).edit();
  }

  //获取SharedPreferences实例
  public static SharedPreferences getSharedPreferences(Context context) {
    return context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
  }
}