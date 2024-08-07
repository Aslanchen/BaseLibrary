package com.aslan.baselibrary.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Method;

/**
 * 文件操作类
 *
 * @author Aslan
 * @date 2018/4/11
 */
public final class FileUtil {

  public static final String EXT_STORAGE_PATH = FileUtil.getExtStoragePath();
  public static final String EXT_STORAGE_DIR = EXT_STORAGE_PATH + File.separator;
  public static final String APP_EXT_STORAGE_PATH = EXT_STORAGE_DIR + "Android";
  public static final String EXT_DOWNLOADS_PATH = FileUtil.getExtDownloadsPath();
  public static final String EXT_PICTURES_PATH = FileUtil.getExtPicturesPath();
  public static final String EXT_DCIM_PATH = FileUtil.getExtDCIMPath();

  private static final String LOG = "Logs";

  public static File getLog(Context context) {
    return getFilesDir(context, LOG);
  }

  public static File getDownload(Context context) {
    return getFilesDir(context, Environment.DIRECTORY_DOWNLOADS);
  }

  public static File getDocument(Context context) {
    return getFilesDir(context, Environment.DIRECTORY_DOCUMENTS);
  }

  public static File getPhoto(Context context) {
    return getFilesDir(context, Environment.DIRECTORY_PICTURES);
  }

  /**
   * 获取 Android 系统根目录
   * <pre>path: /system</pre>
   *
   * @return 系统根目录
   */
  public static String getRootPath() {
    return Environment.getRootDirectory().getAbsolutePath();
  }

  /**
   * 获取 data 目录
   * <pre>path: /data</pre>
   *
   * @return data 目录
   */
  public static String getDataPath() {
    return Environment.getDataDirectory().getAbsolutePath();
  }

  /**
   * 获取缓存目录
   * <pre>path: data/cache</pre>
   *
   * @return 缓存目录
   */
  public static String getIntDownloadCachePath() {
    return Environment.getDownloadCacheDirectory().getAbsolutePath();
  }

  //===============================内置私有存储空间===========================================//

  /**
   * 获取此应用的缓存目录
   * <pre>path: /data/data/package/cache</pre>
   *
   * @return 此应用的缓存目录
   */
  public static String getAppIntCachePath(Context context) {
    return context.getCacheDir().getAbsolutePath();
  }

  /**
   * 获取此应用的文件目录
   * <pre>path: /data/data/package/files</pre>
   *
   * @return 此应用的文件目录
   */
  public static String getAppIntFilesPath(Context context) {
    return context.getFilesDir().getAbsolutePath();
  }

  /**
   * 获取此应用的数据库文件目录
   * <pre>path: /data/data/package/databases/name</pre>
   *
   * @param name 数据库文件名
   * @return 数据库文件目录
   */
  public static String getAppIntDbPath(Context context, String name) {
    return context.getDatabasePath(name).getAbsolutePath();
  }

  //===============================外置公共存储空间，这部分需要获取读取权限，并且在Android10以后文件读取都需要使用ContentResolver进行操作===========================================//

  /**
   * 是否是公有目录
   *
   * @return 是否是公有目录
   */
  public static boolean isPublicPath(File file) {
    if (file == null) {
      return false;
    }
    try {
      return isPublicPath(file.getCanonicalPath());
    } catch (IOException e) {
      e.printStackTrace();
    }
    return false;
  }

  /**
   * 是否是公有目录
   *
   * @return 是否是公有目录
   */
  public static boolean isPublicPath(String filePath) {
    if (TextUtils.isEmpty(filePath)) {
      return false;
    }
    return filePath.startsWith(EXT_STORAGE_PATH) && !filePath.startsWith(APP_EXT_STORAGE_PATH);
  }

  /**
   * 获取 Android 外置储存的根目录
   * <pre>path: /storage/emulated/0</pre>
   *
   * @return 外置储存根目录
   */
  public static String getExtStoragePath() {
    return Environment.getExternalStorageDirectory().getAbsolutePath();
  }

  /**
   * 获取闹钟铃声目录
   * <pre>path: /storage/emulated/0/Alarms</pre>
   *
   * @return 闹钟铃声目录
   */
  public static String getExtAlarmsPath() {
    return Environment
        .getExternalStoragePublicDirectory(Environment.DIRECTORY_ALARMS)
        .getAbsolutePath();
  }

  /**
   * 获取相机拍摄的照片和视频的目录
   * <pre>path: /storage/emulated/0/DCIM</pre>
   *
   * @return 照片和视频目录
   */
  public static String getExtDCIMPath() {
    return Environment
        .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
        .getAbsolutePath();
  }

  /**
   * 获取文档目录
   * <pre>path: /storage/emulated/0/Documents</pre>
   *
   * @return 文档目录
   */
  @RequiresApi(api = Build.VERSION_CODES.KITKAT)
  public static String getExtDocumentsPath() {
    return Environment
        .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
        .getAbsolutePath();
  }

  /**
   * 获取下载目录
   * <pre>path: /storage/emulated/0/Download</pre>
   *
   * @return 下载目录
   */
  public static String getExtDownloadsPath() {
    return Environment
        .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        .getAbsolutePath();
  }

  /**
   * 获取视频目录
   * <pre>path: /storage/emulated/0/Movies</pre>
   *
   * @return 视频目录
   */
  public static String getExtMoviesPath() {
    return Environment
        .getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES)
        .getAbsolutePath();
  }

  /**
   * 获取音乐目录
   * <pre>path: /storage/emulated/0/Music</pre>
   *
   * @return 音乐目录
   */
  public static String getExtMusicPath() {
    return Environment
        .getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)
        .getAbsolutePath();
  }

  /**
   * 获取提示音目录
   * <pre>path: /storage/emulated/0/Notifications</pre>
   *
   * @return 提示音目录
   */
  public static String getExtNotificationsPath() {
    return Environment
        .getExternalStoragePublicDirectory(Environment.DIRECTORY_NOTIFICATIONS)
        .getAbsolutePath();
  }

  /**
   * 获取图片目录
   * <pre>path: /storage/emulated/0/Pictures</pre>
   *
   * @return 图片目录
   */
  public static String getExtPicturesPath() {
    return Environment
        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        .getAbsolutePath();
  }

  /**
   * 获取 Podcasts 目录
   * <pre>path: /storage/emulated/0/Podcasts</pre>
   *
   * @return Podcasts 目录
   */
  public static String getExtPodcastsPath() {
    return Environment
        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PODCASTS)
        .getAbsolutePath();
  }

  /**
   * 获取铃声目录
   * <pre>path: /storage/emulated/0/Ringtones</pre>
   *
   * @return 下载目录
   */
  public static String getExtRingtonesPath() {
    return Environment
        .getExternalStoragePublicDirectory(Environment.DIRECTORY_RINGTONES)
        .getAbsolutePath();
  }

  //===============================外置内部存储空间，这部分不需要获取读取权限===========================================//

  /**
   * 获取此应用在外置储存中的缓存目录
   * <pre>path: /storage/emulated/0/Android/data/package/cache</pre>
   *
   * @return 此应用在外置储存中的缓存目录
   */
  public static String getAppExtCachePath(Context context) {
    return getFilePath(context.getExternalCacheDir());
  }

  /**
   * 获取此应用在外置储存中的文件目录
   * <pre>path: /storage/emulated/0/Android/data/package/files</pre>
   *
   * @return 此应用在外置储存中的文件目录
   */
  public static String getAppExtFilePath(Context context) {
    return getFilePath(context.getExternalFilesDir(null));
  }

  /**
   * 获取此应用在外置储存中的闹钟铃声目录
   * <pre>path: /storage/emulated/0/Android/data/package/files/Alarms</pre>
   *
   * @return 此应用在外置储存中的闹钟铃声目录
   */
  public static String getAppExtAlarmsPath(Context context) {
    return getFilePath(context.getExternalFilesDir(Environment.DIRECTORY_ALARMS));
  }

  /**
   * 获取此应用在外置储存中的相机目录
   * <pre>path: /storage/emulated/0/Android/data/package/files/DCIM</pre>
   *
   * @return 此应用在外置储存中的相机目录
   */
  public static String getAppExtDCIMPath(Context context) {
    return getFilePath(context.getExternalFilesDir(Environment.DIRECTORY_DCIM));
  }

  /**
   * 获取此应用在外置储存中的文档目录
   * <pre>path: /storage/emulated/0/Android/data/package/files/Documents</pre>
   *
   * @return 此应用在外置储存中的文档目录
   */
  @RequiresApi(api = Build.VERSION_CODES.KITKAT)
  public static String getAppExtDocumentsPath(Context context) {
    return getFilePath(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS));
  }

  /**
   * 获取此应用在外置储存中的下载目录
   * <pre>path: /storage/emulated/0/Android/data/package/files/Download</pre>
   *
   * @return 此应用在外置储存中的下载目录
   */
  public static String getAppExtDownloadPath(Context context) {
    return getFilePath(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS));
  }

  /**
   * 获取此应用在外置储存中的视频目录
   * <pre>path: /storage/emulated/0/Android/data/package/files/Movies</pre>
   *
   * @return 此应用在外置储存中的视频目录
   */
  public static String getAppExtMoviesPath(Context context) {
    return getFilePath(context.getExternalFilesDir(Environment.DIRECTORY_MOVIES));
  }

  /**
   * 获取此应用在外置储存中的音乐目录
   * <pre>path: /storage/emulated/0/Android/data/package/files/Music</pre>
   *
   * @return 此应用在外置储存中的音乐目录
   */
  public static String getAppExtMusicPath(Context context) {
    return getFilePath(context.getExternalFilesDir(Environment.DIRECTORY_MUSIC));
  }

  /**
   * 获取此应用在外置储存中的提示音目录
   * <pre>path: /storage/emulated/0/Android/data/package/files/Notifications</pre>
   *
   * @return 此应用在外置储存中的提示音目录
   */
  public static String getAppExtNotificationsPath(Context context) {
    return getFilePath(context.getExternalFilesDir(Environment.DIRECTORY_NOTIFICATIONS));
  }

  /**
   * 获取此应用在外置储存中的图片目录
   * <pre>path: /storage/emulated/0/Android/data/package/files/Pictures</pre>
   *
   * @return 此应用在外置储存中的图片目录
   */
  public static String getAppExtPicturesPath(Context context) {
    return getFilePath(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES));
  }

  /**
   * 获取此应用在外置储存中的 Podcasts 目录
   * <pre>path: /storage/emulated/0/Android/data/package/files/Podcasts</pre>
   *
   * @return 此应用在外置储存中的 Podcasts 目录
   */
  public static String getAppExtPodcastsPath(Context context) {
    return getFilePath(context.getExternalFilesDir(Environment.DIRECTORY_PODCASTS));
  }

  /**
   * 获取此应用在外置储存中的铃声目录
   * <pre>path: /storage/emulated/0/Android/data/package/files/Ringtones</pre>
   *
   * @return 此应用在外置储存中的铃声目录
   */
  public static String getAppExtRingtonesPath(Context context) {
    return getFilePath(context.getExternalFilesDir(Environment.DIRECTORY_RINGTONES));
  }

  private static String getFilePath(File file) {
    return file != null ? file.getAbsolutePath() : "";
  }

  /**
   * 获取此应用的 Obb 目录
   * <pre>path: /storage/emulated/0/Android/obb/package</pre>
   * <pre>一般用来存放游戏数据包</pre>
   *
   * @return 此应用的 Obb 目录
   */
  public static String getObbPath(Context context) {
    return context.getObbDir().getAbsolutePath();
  }

  /**
   * 将媒体文件转化为资源定位符
   *
   * @param context
   * @param mediaFile 媒体文件
   * @return
   */
  @SuppressLint("Range")
  public static Uri getMediaContentUri(Context context, File mediaFile) {
    String filePath = mediaFile.getAbsolutePath();
    Uri baseUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    Cursor cursor = context.getContentResolver().query(baseUri,
        new String[]{MediaStore.Images.Media._ID}, MediaStore.Images.Media.DATA + "=? ",
        new String[]{filePath}, null);
    if (cursor != null && cursor.moveToFirst()) {
      int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
      cursor.close();
      return Uri.withAppendedPath(baseUri, "" + id);
    } else {
      if (mediaFile.exists()) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DATA, filePath);
        return context.getContentResolver().insert(baseUri, values);
      }
      return null;
    }
  }

  @SuppressLint("Range")
  @RequiresApi(api = Build.VERSION_CODES.Q)
  public static Uri getDownloadContentUri(Context context, File file) {
    String filePath = file.getAbsolutePath();
    Uri baseUri = MediaStore.Downloads.EXTERNAL_CONTENT_URI;
    Cursor cursor = context.getContentResolver().query(baseUri,
        new String[]{MediaStore.Downloads._ID}, MediaStore.Downloads.DATA + "=? ",
        new String[]{filePath}, null);
    if (cursor != null && cursor.moveToFirst()) {
      int id = cursor.getInt(cursor.getColumnIndex(MediaStore.DownloadColumns._ID));
      cursor.close();
      return Uri.withAppendedPath(baseUri, "" + id);
    } else {
      if (file.exists()) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Downloads.DATA, filePath);
        return context.getContentResolver().insert(baseUri, values);
      }
      return null;
    }
  }

  /**
   * Return a content URI for a given file.
   *
   * @param file The file.
   * @return a content URI for a given file
   */
  @Nullable
  public static Uri getUriForFile(Context context, String authority, final File file) {
    if (file == null) {
      return null;
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      return FileProvider.getUriForFile(context, authority, file);
    } else {
      return Uri.fromFile(file);
    }
  }

  /**
   * 根据文件获取uri
   *
   * @param filePath 文件路径
   * @return
   */
  public static Uri getUriByFilePath(Context context, String authority, final String filePath) {
    return getUriByFile(context, authority, FileUtil.getFileByPath(filePath));
  }

  /**
   * 根据文件获取uri
   *
   * @param file 文件
   * @return
   */
  public static Uri getUriByFile(Context context, String authority, final File file) {
    if (file == null) {
      return null;
    }
    if (SAFUtils.isScopedStorageMode() && isPublicPath(file)) {
      String filePath = file.getAbsolutePath();
      if (filePath.startsWith(EXT_DOWNLOADS_PATH)) {
        return getDownloadContentUri(context, file);
      } else if (filePath.startsWith(EXT_PICTURES_PATH) || filePath.startsWith(EXT_DCIM_PATH)) {
        return getMediaContentUri(context, file);
      } else {
        return getUriForFile(context, authority, file);
      }
    } else {
      return getUriForFile(context, authority, file);
    }
  }

  /**
   * Uri to file.
   *
   * @param uri        The uri.
   * @param columnName The name of the target column.
   *                   <p>e.g. {@link MediaStore.Images.Media#DATA}</p>
   * @return file
   */
  public static File uri2File(Context context, final Uri uri, final String columnName) {
    if (uri == null) {
      return null;
    }

    CursorLoader cl = new CursorLoader(context);
    cl.setUri(uri);
    cl.setProjection(new String[]{columnName});
    Cursor cursor = cl.loadInBackground();
    int columnIndex = cursor.getColumnIndexOrThrow(columnName);
    cursor.moveToFirst();
    File file = new File(cursor.getString(columnIndex));
    cursor.close();
    return file;
  }

  /**
   * 根据uri获取文件的绝对路径，解决Android 4.4以上 根据uri获取路径的方法
   *
   * @param context 上下文
   * @param uri     资源路径
   * @return 文件路径
   */
  public static String getFilePathByUri(Context context, Uri uri) {
    if (context == null || uri == null) {
      return null;
    }

    String scheme = uri.getScheme();
    // 以 file:// 开头的
    if (ContentResolver.SCHEME_FILE.equals(scheme)) {
      return uri.getPath();
    }
    // 以 content:// 开头的，比如 content://media/extenral/images/media/17766
    if (ContentResolver.SCHEME_CONTENT.equals(scheme) && Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
      String path = null;
      Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.Media.DATA}, null, null, null);
      if (cursor != null) {
        if (cursor.moveToFirst()) {
          int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
          if (columnIndex > -1) {
            path = cursor.getString(columnIndex);
          }
        }
        cursor.close();
      }
      return path;
    }

    // 4.4及之后的 是以 content:// 开头的，比如 content://com.android.providers.media.documents/document/image%3A235700
    if (ContentResolver.SCHEME_CONTENT.equals(scheme) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(
        context, uri)) {
      if (isExternalStorageDocument(uri)) {
        String docId = DocumentsContract.getDocumentId(uri);
        String[] split = docId.split(":");
        if (split.length == 2) {
          final String type = split[0];
          if ("primary".equalsIgnoreCase(type)) {
            return Environment.getExternalStorageDirectory() + "/" + split[1];
          } else {
            StorageManager storageManager = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
            try {
              Class<?> storageVolumeClazz = Class.forName("android.os.storage.StorageVolume");
              Method getVolumeList = storageManager.getClass().getMethod("getVolumeList");
              Method getUuid = storageVolumeClazz.getMethod("getUuid");
              Method getState = storageVolumeClazz.getMethod("getState");
              Method getPath = storageVolumeClazz.getMethod("getPath");
              Method isPrimary = storageVolumeClazz.getMethod("isPrimary");
              Method isEmulated = storageVolumeClazz.getMethod("isEmulated");

              Object result = getVolumeList.invoke(storageManager);

              final int length = Array.getLength(result);
              for (int i = 0; i < length; i++) {
                Object storageVolumeElement = Array.get(result, i);
                final boolean mounted = Environment.MEDIA_MOUNTED.equals(getState.invoke(storageVolumeElement))
                    || Environment.MEDIA_MOUNTED_READ_ONLY.equals(getState.invoke(storageVolumeElement));
                //if the media is not mounted, we need not get the volume details
                if (!mounted) {
                  continue;
                }
                //Primary storage is already handled.
                if ((Boolean) isPrimary.invoke(storageVolumeElement)
                    && (Boolean) isEmulated.invoke(storageVolumeElement)) {
                  continue;
                }
                String uuid = (String) getUuid.invoke(storageVolumeElement);
                if (uuid != null && uuid.equals(type)) {
                  return getPath.invoke(storageVolumeElement) + "/" + split[1];
                }
              }
            } catch (Exception ex) {
              ex.printStackTrace();
            }
          }
        }
      } else if (isDownloadsDocument(uri)) {
        String documentId = DocumentsContract.getDocumentId(uri);

        if (TextUtils.isEmpty(documentId)) {
          return null;
        }
        if (documentId.startsWith("raw:")) {
          return documentId.substring("raw:".length());
        }

        if (documentId.startsWith("msf:") && Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
          String[] split = documentId.split(":");
          if (split.length == 2) {
            // content://media/external/downloads
            Uri contentUri = MediaStore.Downloads.EXTERNAL_CONTENT_URI;
            String selection = MediaStore.Downloads._ID + "=?";
            String[] selectionArgs = new String[]{split[1]};
            return getDataColumn(context, contentUri, selection, selectionArgs);
          }
        }

        long id = Long.parseLong(documentId);
        if (id != -1) {
          return getDownloadPathById(context, id);
        }
      } else if (isMediaDocument(uri)) {
        String docId = DocumentsContract.getDocumentId(uri);
        String[] split = docId.split(":");
        String type = split[0];
        Uri contentUri = null;
        if ("image".equals(type)) {
          contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        } else if ("video".equals(type)) {
          contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        } else if ("audio".equals(type)) {
          contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        }
        String selection = MediaStore.Images.Media._ID + "=?";
        String[] selectionArgs = new String[]{split[1]};
        return getDataColumn(context, contentUri, selection, selectionArgs);
      }
    } else if (ContentResolver.SCHEME_CONTENT.equalsIgnoreCase(scheme)) {
      if (isGooglePhotosUri(uri)) {
        return uri.getLastPathSegment();
      } else if (isHuaWeiUri(uri)) {
        String uriPath = uri.getPath();
        if (!TextUtils.isEmpty(uriPath) && uriPath.startsWith("/root")) {
          return uriPath.replace("/root", "");
        }
      } else if (isQQUri(uri)) {
        String uriPath = uri.getPath();
        if (!TextUtils.isEmpty(uriPath)) {
          return Environment.getExternalStorageDirectory() + uriPath.substring("/QQBrowser".length());
        }
      }
      return getDataColumn(context, uri, null, null);
    } else if (ContentResolver.SCHEME_FILE.equalsIgnoreCase(scheme)) {
      return uri.getPath();
    }
    return null;
  }

  @Nullable
  private static String getDownloadPathById(Context context, long id) {
    Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), id);
    return getDataColumn(context, contentUri, null, null);
  }


  private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
    Cursor cursor = null;
    String column = MediaStore.Images.Media.DATA;
    String[] projection = {column};
    try {
      cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
      if (cursor != null && cursor.moveToFirst()) {
        int index = cursor.getColumnIndexOrThrow(column);
        return cursor.getString(index);
      }
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
    } finally {
      if (cursor != null) {
        cursor.close();
      }
    }
    return null;
  }

  private static final String AUTHORITY_EXTERNAL_STORAGE_DOCUMENT = "com.android.externalstorage.documents";
  private static final String AUTHORITY_DOWNLOADS_DOCUMENT = "com.android.providers.downloads.documents";
  private static final String AUTHORITY_MEDIA_DOCUMENT = "com.android.providers.media.documents";

  /**
   * @param uri The Uri to check.
   * @return Whether the Uri authority is ExternalStorageProvider.
   */
  public static boolean isExternalStorageDocument(Uri uri) {
    return AUTHORITY_EXTERNAL_STORAGE_DOCUMENT.equals(uri.getAuthority());
  }

  /**
   * @param uri The Uri to check.
   * @return Whether the Uri authority is DownloadsProvider.
   */
  public static boolean isDownloadsDocument(Uri uri) {
    return AUTHORITY_DOWNLOADS_DOCUMENT.equals(uri.getAuthority());
  }

  /**
   * @param uri The Uri to check.
   * @return Whether the Uri authority is MediaProvider.
   */
  public static boolean isMediaDocument(Uri uri) {
    return AUTHORITY_MEDIA_DOCUMENT.equals(uri.getAuthority());
  }

  /**
   * @param uri The Uri to check.
   * @return Whether the Uri authority is Google Photos.
   */
  public static boolean isGooglePhotosUri(Uri uri) {
    return "com.google.android.apps.photos.content".equals(uri.getAuthority());
  }

  /**
   * content://com.huawei.hidisk.fileprovider/root/storage/emulated/0/Android/data/com.xxx.xxx/
   *
   * @param uri uri The Uri to check.
   * @return Whether the Uri authority is HuaWei Uri.
   */
  public static boolean isHuaWeiUri(Uri uri) {
    return "com.huawei.hidisk.fileprovider".equals(uri.getAuthority());
  }

  /**
   * content://com.tencent.mtt.fileprovider/QQBrowser/Android/data/com.xxx.xxx/
   *
   * @param uri uri The Uri to check.
   * @return Whether the Uri authority is QQ Uri.
   */
  public static boolean isQQUri(Uri uri) {
    return "com.tencent.mtt.fileprovider".equals(uri.getAuthority());
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
   *
   * @param file           文件夹
   * @param isDeleteDirect 是否需要删除文件夹
   */
  private void deleteFile(File file, boolean isDeleteDirect) {
    if (file == null) {
      return;
    }

    if (file.isDirectory()) {
      File[] files = file.listFiles();
      for (File f : files) {
        deleteFile(f, isDeleteDirect);
      }

      if (isDeleteDirect) {
        file.delete();
      }
    } else if (file.exists()) {
      file.delete();
    }
  }

  /**
   * 把asset下文件copy到SD卡
   *
   * @param context   上下文
   * @param assetName 资源文件名称，完整路径
   * @param output    目标文件
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

  /**
   * 根据文件路径获取文件
   *
   * @param filePath 文件路径
   * @return 文件
   */
  @Nullable
  public static File getFileByPath(final String filePath) {
    return isSpace(filePath) ? null : new File(filePath);
  }

  private static boolean isSpace(final String s) {
    if (s == null) {
      return true;
    }
    for (int i = 0, len = s.length(); i < len; ++i) {
      if (!Character.isWhitespace(s.charAt(i))) {
        return false;
      }
    }
    return true;
  }

  /**
   * 判断文件是否存在
   *
   * @param file 文件
   * @return {@code true}: 存在<br>{@code false}: 不存在
   */
  public static boolean isFileExists(Context context, final File file) {
    if (file == null) {
      return false;
    }
    if (file.exists()) {
      return true;
    }
    return isFileExists(context, file.getAbsolutePath());
  }

  /**
   * 判断文件是否存在
   *
   * @param filePath 文件路径
   * @return {@code true}: 存在<br>{@code false}: 不存在
   */
  public static boolean isFileExists(Context context, final String filePath) {
    File file = getFileByPath(filePath);
    if (file == null) {
      return false;
    }
    if (file.exists()) {
      return true;
    }
    return isFileExistsApi29(context, filePath);
  }

  /**
   * Android 10判断文件是否存在的方法
   *
   * @param filePath 文件路径
   * @return {@code true}: 存在<br>{@code false}: 不存在
   */
  private static boolean isFileExistsApi29(Context context, String filePath) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
      AssetFileDescriptor afd = null;
      try {
        Uri uri = Uri.parse(filePath);
        afd = openAssetFileDescriptor(context, uri);
        if (afd == null) {
          return false;
        } else {
          closeIOQuietly(afd);
        }
      } catch (FileNotFoundException e) {
        return false;
      } finally {
        closeIOQuietly(afd);
      }
      return true;
    }
    return false;
  }

  /**
   * 安静关闭 IO
   *
   * @param closeables closeables
   */
  public static void closeIOQuietly(final Closeable... closeables) {
    if (closeables == null) {
      return;
    }
    for (Closeable closeable : closeables) {
      if (closeable != null) {
        try {
          closeable.close();
        } catch (IOException ignored) {
        }
      }
    }
  }

  /**
   * 从uri资源符中读取文件描述
   *
   * @param uri 文本资源符
   * @return AssetFileDescriptor
   */
  public static AssetFileDescriptor openAssetFileDescriptor(Context context, Uri uri) throws FileNotFoundException {
    return getContentResolver(context).openAssetFileDescriptor(uri, SAFUtils.MODE_READ_ONLY);
  }

  private static ContentResolver getContentResolver(Context context) {
    return context.getContentResolver();
  }
}
