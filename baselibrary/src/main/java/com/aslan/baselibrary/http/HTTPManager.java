package com.aslan.baselibrary.http;

import androidx.annotation.NonNull;
import com.elvishew.xlog.Logger;
import com.elvishew.xlog.XLog;
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
public final class HTTPManager {

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
        .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
        .addConverterFactory(GsonConverterFactory.create())
        .build();
    return retrofit.create(service);
  }
}
