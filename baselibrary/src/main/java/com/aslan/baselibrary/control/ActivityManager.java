package com.aslan.baselibrary.control;

import android.app.Activity;
import java.util.ArrayList;
import java.util.List;

/**
 * Activity管理
 *
 * @author Aslanchen
 * @date 2017/4/27
 */
public class ActivityManager {

  private volatile static ActivityManager instance;

  private List<Activity> list;

  private ActivityManager() {
    list = new ArrayList<>();
  }

  public static ActivityManager Instance() {
    if (instance == null) {
      synchronized (ActivityManager.class) {
        if (instance == null) {
          instance = new ActivityManager();
        }
      }
    }
    return instance;
  }

  public void add(Activity activity) {
    list.add(activity);
  }

  public void remove(Activity activity) {
    list.remove(activity);
  }

  public void closeAll() {
    for (Activity activity : list) {
      activity.finish();
    }
    list.clear();
  }
}