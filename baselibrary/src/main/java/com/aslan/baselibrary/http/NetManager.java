package com.aslan.baselibrary.http;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 网络基类
 *
 * @author Aslan
 * @date 2018/4/20
 */
public final class NetManager {

  /**
   * 网络是否可用
   */
  public static boolean isNetworkAvailable(Context context) {
    ConnectivityManager connectivityManager = (ConnectivityManager) context
        .getSystemService(Context.CONNECTIVITY_SERVICE);

    if (null == connectivityManager) {
      return false;
    } else {
      NetworkInfo[] networkInfos = connectivityManager.getAllNetworkInfo();
      if (networkInfos == null) {
        return false;
      } else {
        for (int i = 0; i < networkInfos.length; i++) {
          if (NetworkInfo.State.CONNECTED == networkInfos[i].getState()) {
            return true;
          }
        }
      }
    }

    return false;
  }

  /**
   * 是否为wifi网络
   */
  public final boolean isWifiConnection(Context context) {
    final ConnectivityManager connectivityManager = (ConnectivityManager) context
        .getSystemService(Context.CONNECTIVITY_SERVICE);
    if (null == connectivityManager) {
      return false;
    }

    final NetworkInfo networkInfo = connectivityManager
        .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
    return (networkInfo != null && networkInfo.isConnectedOrConnecting());
  }
}
