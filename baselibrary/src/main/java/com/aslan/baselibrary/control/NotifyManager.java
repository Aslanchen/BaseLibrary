package com.aslan.baselibrary.control;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.provider.Settings;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.support.v4.app.NotificationManagerCompat;

/**
 * 通知栏通知管理类
 *
 * @author Aslanchen
 * @date 2017/4/27
 */
public class NotifyManager {

  private NotificationManagerCompat mNotificationManager;
  private NotificationCompat.Builder builder;
  private Notification notification;
  private Context context;

  public NotifyManager(Context context) {
    this.context = context;
  }

  public NotificationCompat.Builder creat(@DrawableRes int icon, @NonNull String channelId,
      @NonNull String channelName) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      createNotificationChannel(context, channelId, channelName);
    }
    mNotificationManager = NotificationManagerCompat.from(context);
    builder = new NotificationCompat.Builder(context, channelId);
    builder.setSmallIcon(icon);
    return builder;
  }

  @RequiresApi(api = VERSION_CODES.O)
  private void createNotificationChannel(Context context, @NonNull String channelId,
      @NonNull String channelName) {
    NotificationChannel chan = new NotificationChannel(channelId, channelName,
        NotificationManager.IMPORTANCE_NONE);
    NotificationManager notificationManager = (NotificationManager) context
        .getSystemService(Context.NOTIFICATION_SERVICE);
    notificationManager.createNotificationChannel(chan);
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

  public void gotoOpenNotification(Context context) {
    if (Build.VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN_MR2) {
      gotoNotifyservice(context);
    } else {
      gotoAccessibility(context);
    }
  }

  @RequiresApi(api = VERSION_CODES.JELLY_BEAN_MR2)
  public void gotoNotifyservice(Context context) {
    try {
      Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
      context.startActivity(intent);
    } catch (ActivityNotFoundException anfe) {
      try {
        Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
        context.startActivity(intent);
      } catch (ActivityNotFoundException anfe2) {
      }
    }
  }

  public void gotoAccessibility(Context context) {
    try {
      Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
      context.startActivity(intent);
    } catch (ActivityNotFoundException anfe) {
      try {
        Intent intent = new Intent(Settings.ACTION_SETTINGS);
        context.startActivity(intent);
      } catch (ActivityNotFoundException anfe2) {
      }
    }
  }
}
