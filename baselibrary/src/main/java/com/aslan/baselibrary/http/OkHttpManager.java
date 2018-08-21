package com.aslan.baselibrary.http;

import com.aslan.baselibrary.BuildConfig;
import com.aslan.baselibrary.http.builder.GetBuilder;
import com.aslan.baselibrary.http.builder.OtherRequestBuilder;
import com.aslan.baselibrary.http.builder.PostFormBuilder;
import com.aslan.baselibrary.http.builder.PostStringBuilder;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;

/**
 * Okhttp单列
 */
public class OkHttpManager {

  private volatile static OkHttpManager instance;

  public static final long DEFAULT_MILLISECONDS = 10_000L;
  private OkHttpClient mOkHttpClient;

  private OkHttpManager() {
    if (mOkHttpClient == null) {
      OkHttpClient.Builder builder = new OkHttpClient.Builder();
      if (BuildConfig.DEBUG == true) {
        builder.addInterceptor(new OkHttpLogInterceptor());
      }
      builder.connectTimeout(8, TimeUnit.SECONDS);
      builder.writeTimeout(6, TimeUnit.SECONDS);
      builder.readTimeout(8, TimeUnit.SECONDS);
      mOkHttpClient = builder.build();
    }
  }

  public static OkHttpManager getInstance() {
    if (instance == null) {
      synchronized (OkHttpManager.class) {
        if (instance == null) {
          instance = new OkHttpManager();
        }
      }
    }
    return instance;
  }

  public OkHttpClient getOkHttpClient() {
    return mOkHttpClient;
  }

  public static GetBuilder get() {
    return new GetBuilder();
  }

  public static PostStringBuilder postString() {
    return new PostStringBuilder();
  }

  public static OtherRequestBuilder put() {
    return new OtherRequestBuilder(METHOD.PUT);
  }

  public static OtherRequestBuilder delete() {
    return new OtherRequestBuilder(METHOD.DELETE);
  }

  public static OtherRequestBuilder patch() {
    return new OtherRequestBuilder(METHOD.PATCH);
  }

  public static PostFormBuilder post() {
    return new PostFormBuilder();
  }

  public static class METHOD {

    public static final String HEAD = "HEAD";
    public static final String DELETE = "DELETE";
    public static final String PUT = "PUT";
    public static final String PATCH = "PATCH";
  }
}
