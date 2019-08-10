package com.aslan.baselibrary.http;

import android.support.annotation.NonNull;
import android.util.Log;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.http.HttpHeaders;
import okio.Buffer;
import okio.BufferedSource;

/**
 * Okhttp日志打印类
 */
public class OkHttpLogInterceptor implements Interceptor {

  public static final String TAG = "HTTP";

  private static final int MAX = 4000;
  private static final Charset UTF8 = Charset.forName("UTF-8");

  @Override
  public Response intercept(@NonNull Chain chain) throws IOException {
    Request request = chain.request();
    StringBuilder sb = new StringBuilder();
    sb.append(" ");
    sb.append(System.lineSeparator());
    sb.append("--------------------请求开始-------------------------");
    sb.append(System.lineSeparator());
    sb.append(String.format("Heads：%s", request.headers().toString()));
    sb.append(System.lineSeparator());
    sb.append(String.format("Url：%s", request.url()));
    sb.append(System.lineSeparator());
    sb.append(String.format("Method：%s", request.method()));
    sb.append(System.lineSeparator());

    RequestBody requestBody = request.body();
    if (requestBody != null) {
      Buffer buffer = new Buffer();
      requestBody.writeTo(buffer);

      Charset charset = UTF8;
      MediaType contentType = requestBody.contentType();
      if (contentType != null) {
        charset = contentType.charset(UTF8);
      }
      sb.append(String.format("Body：%s", buffer.readString(charset)));
      sb.append(System.lineSeparator());
    }
    sb.append("--------------------请求结束-------------------------");
    sb.append(System.lineSeparator());
    if (sb.length() >= MAX) {
      int start = 0;
      int end = MAX;
      while (start < sb.length()) {
        Log.d(TAG, sb.substring(start, end));
        start = start + MAX;
        end = start + MAX;
        if (end > sb.length()) {
          end = sb.length();
        }
      }
    } else {
      Log.d(TAG, sb.toString());
    }

    sb.delete(0, sb.length());

    sb.append(" ");
    sb.append(System.lineSeparator());
    sb.append("--------------------回复开始-------------------------");
    sb.append(System.lineSeparator());

    Response response = null;
    try {
      response = chain.proceed(request);
    } catch (Exception e) {
      sb.append(e.toString());
      sb.append(e.getMessage());
      sb.append(System.lineSeparator());
      sb.append("--------------------回复结束-------------------------");
      sb.append(System.lineSeparator());
      Log.d(TAG, sb.toString());
      throw e;
    }

    sb.append(System.lineSeparator());
    sb.append(String.format("Heads：%s", String.valueOf(response.headers())));
    sb.append(System.lineSeparator());
    sb.append(String.format("Url：%s", response.request().url()));
    sb.append(System.lineSeparator());
    sb.append(String.format("Code：%s", response.code()));
    sb.append(System.lineSeparator());
    sb.append(String.format("Method：%s", response.request().method()));
    sb.append(System.lineSeparator());

    if (response.isSuccessful()) {

    } else {
      sb.append(String.format("Message：%s", response.message()));
      sb.append(System.lineSeparator());
    }

    if (HttpHeaders.hasBody(response)) {
      ResponseBody responseBody = response.body();
      if (responseBody != null) {
        BufferedSource source = responseBody.source();
        source.request(Long.MAX_VALUE);
        Buffer buffer = source.getBuffer();

        Charset charset = UTF8;
        MediaType contentType = responseBody.contentType();
        if (contentType != null) {
          try {
            charset = contentType.charset(UTF8);
          } catch (UnsupportedCharsetException e) {
            sb.append("--------------------回复结束-------------------------");
            sb.append(System.lineSeparator());
            return response;
          }
        }

        sb.append(String.format("Body：%s", buffer.clone().readString(charset)));
        sb.append(System.lineSeparator());
      }
    }
    sb.append("--------------------回复结束-------------------------");
    sb.append(System.lineSeparator());
    if (sb.length() >= MAX) {
      int start = 0;
      int end = MAX;
      while (start < sb.length()) {
        Log.d(TAG, sb.substring(start, end));
        start = start + MAX;
        end = start + MAX;
        if (end > sb.length()) {
          end = sb.length();
        }
      }
    } else {
      Log.d(TAG, sb.toString());
    }
    return response;
  }
}