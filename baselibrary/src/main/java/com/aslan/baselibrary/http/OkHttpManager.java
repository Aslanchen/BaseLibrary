package com.aslan.baselibrary.http;

import com.aslan.baselibrary.http.builder.GetBuilder;
import com.aslan.baselibrary.http.builder.OtherRequestBuilder;
import com.aslan.baselibrary.http.builder.PostFormBuilder;
import com.aslan.baselibrary.http.builder.PostStringBuilder;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;

/**
 * Okhttp单列
 */
public class OkHttpManager {

  private volatile static OkHttpManager instance;

  public static final long DEFAULT_MILLISECONDS = 10_000L;
  private OkHttpClient mOkHttpClient;
  private Boolean isLogEnable = false;

  private OkHttpManager() {

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

  /**
   * 是否开启日志系统
   *
   * @param isEnable if true open log or not
   */
  public void setLogEnable(boolean isEnable) {
    this.isLogEnable = isEnable;
  }

  public OkHttpClient getOkHttpClient() {
    if (mOkHttpClient == null) {
      Builder builder = newBuilder();
      mOkHttpClient = builder.build();
    }
    return mOkHttpClient;
  }

  public Builder newBuilder() {
    Builder builder = new Builder();
    if (isLogEnable) {
      builder.addInterceptor(new OkHttpLogInterceptor());
    }
    builder.connectTimeout(8, TimeUnit.SECONDS);
    builder.writeTimeout(6, TimeUnit.SECONDS);
    builder.readTimeout(8, TimeUnit.SECONDS);
    return builder;
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
