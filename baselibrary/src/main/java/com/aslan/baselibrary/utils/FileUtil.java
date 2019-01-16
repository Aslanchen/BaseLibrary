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

  private static Boolean isLackPermission(Context context) {
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
    if (isLackPermission(context)) {
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
   * @param assetpath asset下的路径
   * @param SDpath SDpath下保存路径
   */
  public static boolean AssetToSD(Context context, String assetpath, String SDpath) {
    AssetManager asset = context.getAssets();
    FileOutputStream out = null;
    InputStream in = null;
    try {
      File SDFlie = new File(SDpath + File.separatorChar + assetpath);

      File SDFlieDirect = SDFlie.getParentFile();
      if (!SDFlieDirect.exists()) {
        SDFlieDirect.mkdirs();
      }

      if (!SDFlie.exists()) {
        SDFlie.createNewFile();
      }

      in = asset.open(assetpath);
      out = new FileOutputStream(SDFlie);
      byte[] buffer = new byte[1024];
      int byteCount = 0;
      while ((byteCount = in.read(buffer)) != -1) {
        out.write(buffer, 0, byteCount);
      }
      out.flush();
      return true;
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    } finally {
      try {
        if (out != null) {
          out.close();
        }

        if (in != null) {
          in.close();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * 读取assets下的txt文件，返回utf-8 String
   *
   * @param fileName 不包括后缀
   */
  public static String readAssetsTxt(Context context, String fileName) {
    InputStream is = null;
    try {
      is = context.getAssets().open(fileName);
      int size = is.available();
      byte[] buffer = new byte[size];
      is.read(buffer);
      is.close();
      return new String(buffer, "utf-8");
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        is.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return null;
  }
}
