package com.aslan.baselibrary.utils;

import android.content.Context;
import android.content.res.AssetManager;
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

  private static final String LOG = "log";
  private static final String DOWNLOAD = "download";
  private static final String ASSET = "asset";
  private static final String DOCUMENT = "document";
  private static final String PHOTO = "photo";

  private static String log;
  private static String download;
  private static String asset;
  private static String document;
  private static String photo;

  public static String getLog(Context context) {
    if (!TextUtils.isEmpty(log)) {
      return log;
    }

    log = context.getExternalFilesDir(LOG).getAbsolutePath();
    return log;
  }

  public static String getDownload(Context context) {
    if (!TextUtils.isEmpty(download)) {
      return download;
    }

    download = context.getExternalFilesDir(DOWNLOAD).getAbsolutePath();
    return download;
  }

  public static String getAsset(Context context) {
    if (!TextUtils.isEmpty(asset)) {
      return asset;
    }

    asset = context.getExternalFilesDir(ASSET).getAbsolutePath();
    return asset;
  }

  public static String getDocument(Context context) {
    if (!TextUtils.isEmpty(document)) {
      return document;
    }

    document = context.getExternalFilesDir(DOCUMENT).getAbsolutePath();
    return document;
  }

  public static String getPhoto(Context context) {
    if (!TextUtils.isEmpty(photo)) {
      return photo;
    }

    photo = context.getExternalFilesDir(PHOTO).getAbsolutePath();
    return photo;
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
