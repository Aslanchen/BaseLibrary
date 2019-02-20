package com.aslan.baselibrary.http.builder;

import android.arch.lifecycle.Lifecycle.State;
import android.arch.lifecycle.LifecycleOwner;
import android.net.Uri;
import android.text.TextUtils;
import com.aslan.baselibrary.http.request.RequestCall;
import com.aslan.baselibrary.http.request.RequestLifecycleObserver;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class OkHttpRequestBuilder<T extends OkHttpRequestBuilder> {

  protected String url;
  protected Object tag;
  protected String path;
  protected Map<String, String> paramsUrl;
  protected Map<String, String> headers;
  protected LifecycleOwner lifecycleOwner;

  public T with(LifecycleOwner lifecycleOwner) {
    this.lifecycleOwner = lifecycleOwner;
    return (T) this;
  }

  public T host(String host) {
    this.url = host;
    return (T) this;
  }

  public T tag(Object tag) {
    this.tag = tag;
    return (T) this;
  }

  public T headers(Map<String, String> headers) {
    this.headers = headers;
    return (T) this;
  }

  public T addHeader(String key, String val) {
    if (this.headers == null) {
      headers = new LinkedHashMap<>();
    }
    headers.put(key, val);
    return (T) this;
  }

  public T path(String path) {
    this.path = path;
    return (T) this;
  }

  public T paramsUrl(Map<String, String> params) {
    this.paramsUrl = params;
    return (T) this;
  }

  protected String iniUrl(String url, String path, Map<String, String> paramsUrl) {
    if (TextUtils.isEmpty(url)) {
      return url;
    }

    if (TextUtils.isEmpty(path)) {
      return url;
    }

    Uri.Builder builder = Uri.parse(url).buildUpon();
    builder.appendEncodedPath(path);

    if (paramsUrl == null || paramsUrl.isEmpty()) {
      return builder.build().toString();
    }

    if (paramsUrl instanceof LinkedHashMap) {
      for (Map.Entry<String, String> entry : paramsUrl.entrySet()) {
        builder.appendEncodedPath(entry.getValue());
      }
    } else if (paramsUrl instanceof HashMap) {
      for (Map.Entry<String, String> entry : paramsUrl.entrySet()) {
        builder.appendQueryParameter(entry.getKey(), entry.getValue());
      }
    }

    return builder.build().toString();
  }

  public abstract RequestCall buildRequestCall();

  public RequestCall build() {
    if (lifecycleOwner != null
        && lifecycleOwner.getLifecycle().getCurrentState() == State.DESTROYED) {
      throw new IllegalArgumentException("You cannot start a build for a destroyed activity");
    }

    RequestCall requestCall = buildRequestCall();
    if (lifecycleOwner != null) {
      lifecycleOwner.getLifecycle().addObserver(new RequestLifecycleObserver(requestCall));
    }
    return requestCall;
  }
}
