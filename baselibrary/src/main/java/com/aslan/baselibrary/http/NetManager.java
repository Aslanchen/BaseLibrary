package com.aslan.baselibrary.http;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import androidx.annotation.NonNull;
import com.elvishew.xlog.Logger;
import com.elvishew.xlog.XLog;
import io.reactivex.schedulers.Schedulers;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

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

  public static final String TAG_LOG = "OkHttp";
  private static final Logger mLogger = XLog.tag(TAG_LOG).build();

  public static <T> T create(@NonNull String url, Class<T> service) {
    HttpLoggingInterceptor.Logger logger = new HttpLoggingInterceptor.Logger() {
      @Override
      public void log(String message) {
        mLogger.d(message);
      }
    };
    HttpLoggingInterceptor logging = new HttpLoggingInterceptor(logger);
    logging.setLevel(HttpLoggingInterceptor.Level.BODY);
    OkHttpClient okHttpClient = new OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .addInterceptor(logging)
        .build();
    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(url)
        .client(okHttpClient)
        .addCallAdapterFactory(
            RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
        .addConverterFactory(GsonConverterFactory.create())
        .build();
    return retrofit.create(service);
  }
}
