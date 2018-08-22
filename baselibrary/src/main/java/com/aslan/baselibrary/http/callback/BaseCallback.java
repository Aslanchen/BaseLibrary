package com.aslan.baselibrary.http.callback;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.content.Context;
import android.support.annotation.NonNull;
import com.aslan.baselibrary.http.BaseHttpError;
import com.google.gson.Gson;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Okhttp回掉基础类，处理错误异常
 */
public abstract class BaseCallback<T> implements Callback {

  protected static Gson gson = new Gson();

  public LifecycleOwner lifecycleOwner;
  public Context context;

  public BaseCallback(Context context, LifecycleOwner lifecycleOwner) {
    this.context = context;
    this.lifecycleOwner = lifecycleOwner;
  }

  @Override
  public void onFailure(@NonNull Call call, @NonNull IOException e) {
    if (lifecycleOwner != null
        && lifecycleOwner.getLifecycle().getCurrentState() == Lifecycle.State.DESTROYED) {
      onFinish();
      return;
    }

    if (call.isCanceled()) {
      onFinish();
      return;
    }

    onFailure(new BaseHttpError(context, e));
    onFinish();
  }

  @Override
  public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
    if (lifecycleOwner != null
        && lifecycleOwner.getLifecycle().getCurrentState() == Lifecycle.State.DESTROYED) {
      onFinish();
      return;
    }

    if (call.isCanceled()) {
      onFinish();
      return;
    }

    ResponseBody responseBody = response.body();
    if (responseBody == null) {
      onFailure(new BaseHttpError(context));
      return;
    }

    try {
      T data = handleResponse(response);
      if (data != null) {
        onResponse(data);
      }
    } catch (Exception e) {
      onFailure(new BaseHttpError(context));
    }
    onFinish();
  }

  public void onFinish() {

  }

  public abstract void onFailure(BaseHttpError baseHttpError);

  public abstract T handleResponse(@NonNull Response response) throws IOException;

  public abstract void onResponse(T respone);
}
