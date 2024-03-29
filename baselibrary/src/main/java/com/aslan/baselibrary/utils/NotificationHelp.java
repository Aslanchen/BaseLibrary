package com.aslan.baselibrary.utils;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.provider.Settings;
import android.text.TextUtils;
import androidx.annotation.DrawableRes;
import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationCompat.Builder;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 通知栏通知帮助类
 *
 * @author Aslanchen
 * @date 2017/4/27
 */
public final class NotificationHelp {

  /**
   * <p>IMPORTANCE_NONE 关闭通知。
   * <p>IMPORTANCE_MIN 开启通知，不会弹出，但没有提示音，状态栏中无显示。
   * <p>IMPORTANCE_LOW 开启通知，不会弹出，不发出提示音，状态栏中显示。
   * <p>IMPORTANCE_DEFAULT 开启通知，不会弹出，发出提示音，状态栏中显示。
   * <p>IMPORTANCE_HIGH 开启通知，会弹出，发出提示音，状态栏中显示。
   * <p>IMPORTANCE_MAX 开启通知，会弹出，发出提示音，状态栏中显示，允许full screen intents。
   */
  @Retention(RetentionPolicy.SOURCE)
  @IntDef({
      NotificationManagerCompat.IMPORTANCE_NONE,
      NotificationManagerCompat.IMPORTANCE_MIN,
      NotificationManagerCompat.IMPORTANCE_LOW,
      NotificationManagerCompat.IMPORTANCE_DEFAULT,
      NotificationManagerCompat.IMPORTANCE_HIGH,
      NotificationManagerCompat.IMPORTANCE_MAX,
  })
  public @interface Importance {

  }

  private NotificationManagerCompat mNotificationManager;
  private Builder builder;
  private Notification notification;
  private Context context;

  public NotificationHelp(Context context) {
    this.context = context;
  }

  public Builder creatBuilder(@DrawableRes int icon, @NonNull String channelId) {
    return creatBuilder(icon, channelId, null, NotificationManagerCompat.IMPORTANCE_DEFAULT);
  }

  public Builder creatBuilder(@DrawableRes int icon, @NonNull String channelId,
      @Importance int importance) {
    return creatBuilder(icon, channelId, null, importance);
  }

  public Builder creatBuilder(@DrawableRes int icon, @NonNull String channelId,
      @Nullable String channelName) {
    return creatBuilder(icon, channelId, channelName, NotificationManagerCompat.IMPORTANCE_DEFAULT);
  }

  public Builder creatBuilder(@DrawableRes int icon, @NonNull String channelId,
      @Nullable String channelName, @Importance int importance) {
    if (!TextUtils.isEmpty(channelName)) {
      if (VERSION.SDK_INT >= VERSION_CODES.O) {
        assert channelName != null;
        createNotificationChannel(context, channelId, channelName, importance);
      }
    }

    mNotificationManager = NotificationManagerCompat.from(context);
    builder = new Builder(context, channelId);
    if (importance == NotificationManagerCompat.IMPORTANCE_NONE) {
      builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
    } else if (importance == NotificationManagerCompat.IMPORTANCE_MIN) {
      builder.setPriority(NotificationCompat.PRIORITY_MIN);
    } else if (importance == NotificationManagerCompat.IMPORTANCE_LOW) {
      builder.setPriority(NotificationCompat.PRIORITY_LOW);
    } else if (importance == NotificationManagerCompat.IMPORTANCE_DEFAULT) {
      builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
    } else if (importance == NotificationManagerCompat.IMPORTANCE_HIGH) {
      builder.setPriority(NotificationCompat.PRIORITY_HIGH);
    } else if (importance == NotificationManagerCompat.IMPORTANCE_MAX) {
      builder.setPriority(NotificationCompat.PRIORITY_MAX);
    }
    builder.setSmallIcon(icon);
    return builder;
  }

  @RequiresApi(api = VERSION_CODES.O)
  public static void createNotificationChannel(Context context, @NonNull String channelId,
      @NonNull String channelName, @Importance int importance) {
    createNotificationChannel(context, channelId, channelName, importance, null);
  }

  @RequiresApi(api = VERSION_CODES.O)
  public static void createNotificationChannel(Context context, @NonNull String channelId,
      @NonNull String channelName, @Importance int importance,
      @Nullable IChannelSetting iChannelSetting) {
    NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
    NotificationManager notificationManager = (NotificationManager) context
        .getSystemService(Context.NOTIFICATION_SERVICE);
    if (iChannelSetting != null) {
      iChannelSetting.onChannelSetting(channel);
    }
    notificationManager.createNotificationChannel(channel);
  }

  public Notification build() {
    builder.setWhen(System.currentTimeMillis());
    notification = builder.build();
    return notification;
  }

  public Notification getNotification() {
    return notification;
  }

  public Builder getBuilder() {
    return builder;
  }

  public void notify(int id) {
    Notification mNotification = build();
    if (mNotificationManager != null) {
      mNotificationManager.notify(id, mNotification);
    }
  }

  public static void cancel(Context context, int id) {
    NotificationManagerCompat.from(context).cancel(id);
  }

  public static void cancelAll(Context context) {
    NotificationManagerCompat.from(context).cancelAll();
  }

  /**
   * 通知中某个channel是否开启
   */
  @RequiresApi(api = VERSION_CODES.O)
  public static boolean isChannelEnabled(Context context, String channelId) {
    NotificationManager manager = (NotificationManager) context
        .getSystemService(Context.NOTIFICATION_SERVICE);
    NotificationChannel channel = manager.getNotificationChannel(channelId);
    return channel.getImportance() != NotificationManager.IMPORTANCE_NONE;
  }

  /**
   * 通知是否可用，针对4.4以上的系统才有效，4.4以下返回true
   */
  public static boolean isEnabled(Context context) {
    NotificationManagerCompat manager = NotificationManagerCompat.from(context);
    return manager.areNotificationsEnabled();
  }

  /**
   * 打开channel设置界面
   */
  @RequiresApi(api = VERSION_CODES.O)
  public static void openChannelSetting(Activity activity, String channelId, int requestCode) {
    Intent intent = new Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
    intent.putExtra(Settings.EXTRA_APP_PACKAGE, activity.getPackageName());
    intent.putExtra(Settings.EXTRA_CHANNEL_ID, channelId);
    if (activity.getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY)
        != null) {
      activity.startActivityForResult(intent, requestCode);
    }
  }

  /**
   * 打开channel设置界面
   */
  @RequiresApi(api = VERSION_CODES.O)
  public static void openChannelSetting(Fragment fragment, String channelId, int requestCode) {
    Intent intent = new Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
    intent.putExtra(Settings.EXTRA_APP_PACKAGE, fragment.getContext().getPackageName());
    intent.putExtra(Settings.EXTRA_CHANNEL_ID, channelId);
    if (fragment.getContext().getPackageManager()
        .resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY)
        != null) {
      fragment.startActivityForResult(intent, requestCode);
    }
  }

  /**
   * 设置通知开关
   */
  public static void openNotificationSetting(Activity activity, int requestCode) {
    try {
      if (VERSION.SDK_INT >= VERSION_CODES.O) {
        //这种方案适用于 API 26, 即8.0（含8.0）以上可以用
        ApplicationInfo appInfo = activity.getApplicationInfo();
        String pkg = activity.getApplicationContext().getPackageName();
        int uid = appInfo.uid;
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
        intent.putExtra(Settings.EXTRA_APP_PACKAGE, pkg);
        intent.putExtra(Settings.EXTRA_CHANNEL_ID, uid);
        activity.startActivityForResult(intent, requestCode);
      } else if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
        //这种方案适用于 API21——25，即 5.0——7.1 之间的版本可以使用
        ApplicationInfo appInfo = activity.getApplicationInfo();
        String pkg = activity.getApplicationContext().getPackageName();
        int uid = appInfo.uid;
        Intent intent = new Intent();
        intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
        intent.putExtra("app_package", pkg);
        intent.putExtra("app_uid", uid);
        activity.startActivityForResult(intent, requestCode);
      } else if (VERSION.SDK_INT == VERSION_CODES.KITKAT) {
        ApplicationInfo appInfo = activity.getApplicationInfo();
        String pkg = activity.getApplicationContext().getPackageName();
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setData(Uri.parse("package:" + pkg));
        activity.startActivityForResult(intent, requestCode);
      } else {
        Intent intent = new Intent(Settings.ACTION_SETTINGS);
        activity.startActivityForResult(intent, requestCode);
      }
    } catch (Exception e) {
      Intent intent = new Intent(Settings.ACTION_SETTINGS);
      activity.startActivityForResult(intent, requestCode);
    }
  }

  /**
   * 设置通知开关
   */
  public static void openNotificationSetting(Fragment fragment, int requestCode) {
    try {
      if (VERSION.SDK_INT >= VERSION_CODES.O) {
        //这种方案适用于 API 26, 即8.0（含8.0）以上可以用
        ApplicationInfo appInfo = fragment.getContext().getApplicationInfo();
        String pkg = fragment.getContext().getApplicationContext().getPackageName();
        int uid = appInfo.uid;
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
        intent.putExtra(Settings.EXTRA_APP_PACKAGE, pkg);
        intent.putExtra(Settings.EXTRA_CHANNEL_ID, uid);
        fragment.startActivityForResult(intent, requestCode);
      } else if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
        //这种方案适用于 API21——25，即 5.0——7.1 之间的版本可以使用
        ApplicationInfo appInfo = fragment.getContext().getApplicationInfo();
        String pkg = fragment.getContext().getApplicationContext().getPackageName();
        int uid = appInfo.uid;
        Intent intent = new Intent();
        intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
        intent.putExtra("app_package", pkg);
        intent.putExtra("app_uid", uid);
        fragment.startActivityForResult(intent, requestCode);
      } else if (VERSION.SDK_INT == VERSION_CODES.KITKAT) {
        ApplicationInfo appInfo = fragment.getContext().getApplicationInfo();
        String pkg = fragment.getContext().getApplicationContext().getPackageName();
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setData(Uri.parse("package:" + pkg));
        fragment.startActivityForResult(intent, requestCode);
      } else {
        Intent intent = new Intent(Settings.ACTION_SETTINGS);
        fragment.startActivityForResult(intent, requestCode);
      }
    } catch (Exception e) {
      Intent intent = new Intent(Settings.ACTION_SETTINGS);
      fragment.startActivityForResult(intent, requestCode);
    }
  }

  public interface IChannelSetting {

    void onChannelSetting(NotificationChannel channel);
  }
}
