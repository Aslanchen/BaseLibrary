package com.aslan.baselibrary.control;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.provider.Settings;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.support.v4.app.NotificationManagerCompat;

/**
 * 通知栏通知帮助类
 *
 * @author Aslanchen
 * @date 2017/4/27
 */
public final class NotificationHelp {

    private NotificationManagerCompat mNotificationManager;
    private NotificationCompat.Builder builder;
    private Notification notification;
    private Context context;

    public NotificationHelp(Context context) {
        this.context = context;
    }

    public NotificationCompat.Builder creat(@DrawableRes int icon, @NonNull String channelId, @NonNull String channelName) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(context, channelId, channelName, NotificationManager.IMPORTANCE_NONE);
        }
        mNotificationManager = NotificationManagerCompat.from(context);
        builder = new NotificationCompat.Builder(context, channelId);
        builder.setSmallIcon(icon);
        return builder;
    }

    public NotificationCompat.Builder creat(@DrawableRes int icon, @NonNull String channelId, @NonNull String channelName, int importance) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(context, channelId, channelName, importance);
        }
        mNotificationManager = NotificationManagerCompat.from(context);
        builder = new NotificationCompat.Builder(context, channelId);
        builder.setSmallIcon(icon);
        return builder;
    }

    @RequiresApi(api = VERSION_CODES.O)
    private void createNotificationChannel(Context context, @NonNull String channelId, @NonNull String channelName, int importance) {
        NotificationChannel chan = new NotificationChannel(channelId, channelName, importance);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
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

    /**
     * 通知是否可用，针对4.4以上的系统才有效，4.4以下返回true
     *
     * @param context
     * @return
     */
    public static boolean isEnabled(Context context) {
        NotificationManagerCompat manager = NotificationManagerCompat.from(context);
        return manager.areNotificationsEnabled();
    }

    /**
     * 设置通知开关
     *
     * @param activity
     */
    public static void gotoNotificationSetting(Activity activity) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                //这种方案适用于 API 26, 即8.0（含8.0）以上可以用
                ApplicationInfo appInfo = activity.getApplicationInfo();
                String pkg = activity.getApplicationContext().getPackageName();
                int uid = appInfo.uid;
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, pkg);
                intent.putExtra(Settings.EXTRA_CHANNEL_ID, uid);
                activity.startActivityForResult(intent, 100);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //这种方案适用于 API21——25，即 5.0——7.1 之间的版本可以使用
                ApplicationInfo appInfo = activity.getApplicationInfo();
                String pkg = activity.getApplicationContext().getPackageName();
                int uid = appInfo.uid;
                Intent intent = new Intent();
                intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
                intent.putExtra("app_package", pkg);
                intent.putExtra("app_uid", uid);
                activity.startActivityForResult(intent, 100);
            } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
                ApplicationInfo appInfo = activity.getApplicationInfo();
                String pkg = activity.getApplicationContext().getPackageName();
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.setData(Uri.parse("package:" + pkg));
                activity.startActivityForResult(intent, 100);
            } else {
                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                activity.startActivityForResult(intent, 100);
            }
        } catch (Exception e) {
            Intent intent = new Intent(Settings.ACTION_SETTINGS);
            activity.startActivityForResult(intent, 100);
        }
    }

    /**
     * 设置通知开关
     *
     * @param fragment
     */
    public static void gotoNotificationSetting(Fragment fragment) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                //这种方案适用于 API 26, 即8.0（含8.0）以上可以用
                ApplicationInfo appInfo = fragment.getContext().getApplicationInfo();
                String pkg = fragment.getContext().getApplicationContext().getPackageName();
                int uid = appInfo.uid;
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, pkg);
                intent.putExtra(Settings.EXTRA_CHANNEL_ID, uid);
                fragment.startActivityForResult(intent, 100);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //这种方案适用于 API21——25，即 5.0——7.1 之间的版本可以使用
                ApplicationInfo appInfo = fragment.getContext().getApplicationInfo();
                String pkg = fragment.getContext().getApplicationContext().getPackageName();
                int uid = appInfo.uid;
                Intent intent = new Intent();
                intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
                intent.putExtra("app_package", pkg);
                intent.putExtra("app_uid", uid);
                fragment.startActivityForResult(intent, 100);
            } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
                ApplicationInfo appInfo = fragment.getContext().getApplicationInfo();
                String pkg = fragment.getContext().getApplicationContext().getPackageName();
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.setData(Uri.parse("package:" + pkg));
                fragment.startActivityForResult(intent, 100);
            } else {
                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                fragment.startActivityForResult(intent, 100);
            }
        } catch (Exception e) {
            Intent intent = new Intent(Settings.ACTION_SETTINGS);
            fragment.startActivityForResult(intent, 100);
        }
    }
}
