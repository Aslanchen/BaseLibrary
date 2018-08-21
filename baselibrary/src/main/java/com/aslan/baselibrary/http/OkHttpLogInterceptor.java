package com.aslan.baselibrary.http;

import android.support.annotation.NonNull;
import android.util.Log;
import java.io.IOException;
import java.nio.charset.Charset;
import okhttp3.Interceptor;
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

  private static final Charset UTF8 = Charset.forName("UTF-8");

  @Override
  public Response intercept(@NonNull Chain chain) throws IOException {
    Request request = chain.request();
    StringBuilder sb = new StringBuilder();
    sb.append(" ");
    sb.append(System.getProperty("line.separator"));
    sb.append("--------------------请求开始-------------------------");
    sb.append(System.getProperty("line.separator"));
    sb.append(String.format("Heads：%s", request.headers().toString()));
    sb.append(System.getProperty("line.separator"));
    sb.append(String.format("Url：%s", request.url()));
    sb.append(System.getProperty("line.separator"));
    sb.append(String.format("Method：%s", request.method()));
    sb.append(System.getProperty("line.separator"));

    RequestBody requestBody = request.body();
    if (requestBody != null) {
      Buffer buffer = new Buffer();
      requestBody.writeTo(buffer);
      sb.append(String.format("Body：%s", buffer.readString(UTF8)));
      sb.append(System.getProperty("line.separator"));
    }
    sb.append("--------------------请求结束-------------------------");
    sb.append(System.getProperty("line.separator"));
    Log.e(TAG, sb.toString());

    sb.delete(0, sb.length());
    Response response = chain.proceed(request);
    sb.append(" ");
    sb.append(System.getProperty("line.separator"));
    sb.append("--------------------回复开始-------------------------");
    sb.append(System.getProperty("line.separator"));
    sb.append(String.format("Heads：%s", response.headers().toString()));
    sb.append(System.getProperty("line.separator"));
    sb.append(String.format("Url：%s", response.request().url()));
    sb.append(System.getProperty("line.separator"));
    sb.append(String.format("Code：%s", response.code()));
    sb.append(System.getProperty("line.separator"));
    sb.append(String.format("Method：%s", response.request().method()));
    sb.append(System.getProperty("line.separator"));

    if (response.isSuccessful()) {

    } else {
      sb.append(String.format("Message：%s", response.message()));
      sb.append(System.getProperty("line.separator"));
    }

    ResponseBody responseBody = response.body();
    if (HttpHeaders.hasBody(response)) {
      if (responseBody != null) {
        BufferedSource source = responseBody.source();
        source.request(Long.MAX_VALUE);
        Buffer buffer = source.buffer();
        sb.append(String.format("Body：%s", buffer.clone().readString(UTF8)));
        sb.append(System.getProperty("line.separator"));
      }
    }
    sb.append("--------------------回复结束-------------------------");
    sb.append(System.getProperty("line.separator"));
    Log.e(TAG, sb.toString());
    return response.newBuilder().body(responseBody).build();
  }
}
