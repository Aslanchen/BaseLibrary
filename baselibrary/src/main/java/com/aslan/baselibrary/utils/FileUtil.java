package com.aslan.baselibrary.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 文件操作类
 *
 * @author Aslan
 * @date 2018/4/11
 */
public final class FileUtil {

  private static final String LOG = "Logs";

  private static String log;
  private static String download;
  private static String document;
  private static String photo;

  public static String getLog(Context context) {
    if (!TextUtils.isEmpty(log)) {
      return log;
    }

    log = getFilesDir(context, LOG).getAbsolutePath();
    return log;
  }

  public static String getDownload(Context context) {
    if (!TextUtils.isEmpty(download)) {
      return download;
    }

    download = getFilesDir(context, Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
    return download;
  }

  public static String getDocument(Context context) {
    if (!TextUtils.isEmpty(document)) {
      return document;
    }

    document = getFilesDir(context, Environment.DIRECTORY_DOCUMENTS).getAbsolutePath();
    return document;
  }

  public static String getPhoto(Context context) {
    if (!TextUtils.isEmpty(photo)) {
      return photo;
    }

    photo = getFilesDir(context, Environment.DIRECTORY_PICTURES).getAbsolutePath();
    return photo;
  }

  /**
   * 判断sd卡是否存在
   *
   * @return 如果存在就返回true，反之不存在
   */
  public static boolean isSdCardExist() {
    return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
  }

  /**
   * 是否缺少权限
   *
   * @return true缺少权限，反之不缺少
   */
  public static Boolean isLackPermission(Context context) {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
        && (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        != PackageManager.PERMISSION_GRANTED
        || ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
        != PackageManager.PERMISSION_GRANTED);
  }

  /**
   * 获取本地文件路径,优先采用SD卡
   */
  private static File getFilesDir(Context context, String tag) {
    if (isLackPermission(context) || !isSdCardExist()) {
      return context.getFilesDir();
    } else {
      File file = context.getExternalFilesDir(tag);
      if (file == null) {
        return context.getFilesDir();
      } else {
        return file;
      }
    }
  }

  /**
   * 批量删除文件
   */
  private void deleteFile(File file) {
    if (file == null) {
      return;
    }

    if (file.isDirectory()) {
      File[] files = file.listFiles();
      for (File f : files) {
        deleteFile(f);
      }
    } else if (file.exists()) {
      file.delete();
    }
  }

  /**
   * 把asset下文件copy到SD卡
   *
   * @param context 上下文
   * @param assetName 资源文件名称，完整路径
   * @param output 目标文件
   */
  public static void copyAssetFile(Context context, String assetName, File output)
      throws IOException {
    AssetManager asset = context.getAssets();
    FileOutputStream out = null;
    InputStream in = null;
    try {
      in = asset.open(assetName);
      out = new FileOutputStream(output);
      byte[] buffer = new byte[1024];
      int byteCount = 0;
      while ((byteCount = in.read(buffer)) != -1) {
        out.write(buffer, 0, byteCount);
      }
      out.flush();
    } finally {
      try {
        if (out != null) {
          out.close();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }

      try {
        if (in != null) {
          in.close();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
