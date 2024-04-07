package com.aslan.baselibrary.utils;

import android.os.Build;
import android.os.Environment;
import androidx.annotation.StringDef;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class SAFUtils {

  /**
   * 只读模式
   */
  public static final String MODE_READ_ONLY = "r";

  /**
   * 只写模式
   */
  public static final String MODE_WRITE_ONLY = "w";

  /**
   * 读写模式
   */
  public static final String MODE_READ_WRITE = "rw";

  /**
   * 文件读写模式
   */
  @StringDef({MODE_READ_ONLY, MODE_WRITE_ONLY, MODE_READ_WRITE})
  @Retention(RetentionPolicy.SOURCE)
  public @interface FileMode {

  }

  private SAFUtils() {
    throw new UnsupportedOperationException("u can't instantiate me...");
  }

  /**
   * 当前应用是否是以兼容模式运行;
   *
   * @return true: 是，false: 不是
   */
  public static boolean isExternalStorageLegacy() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
      return Environment.isExternalStorageLegacy();
    }
    return false;
  }

  /**
   * 是否是分区存储模式：在公共目录下file的api无效了
   *
   * @return 是否是分区存储模式
   */
  public static boolean isScopedStorageMode() {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && !Environment.isExternalStorageLegacy();
  }
}