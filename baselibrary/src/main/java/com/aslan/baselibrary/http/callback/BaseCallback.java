package com.aslan.baselibrary.http.callback;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.aslan.baselibrary.http.BaseHttpError;
import com.google.gson.Gson;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Okhttp回掉基础类，处理错误异常,以及判断是否已经取消和窗口是否已经被关闭
 */
public abstract class BaseCallback<T> implements Callback {

  protected static Gson gson = new Gson();

  public Context context;

  public BaseCallback(Context context) {
    this.context = context;
  }

  @Override
  public void onFailure(@NonNull Call call, @NonNull IOException e) {
    if (call.isCanceled()) {
      onFinish();
      return;
    }

    onFailure(new BaseHttpError(context, e));
    onFinish();
  }

  @Override
  public void onResponse(@NonNull Call call, @NonNull Response response) {
    if (call.isCanceled()) {
      onFinish();
      return;
    }

    ResponseBody responseBody = response.body();
    if (responseBody == null) {
      onFailure(new BaseHttpError(context));
      onFinish();
      return;
    }

    try {
      T data = handleResponse(response);
      if (data != null) {
        onResponse(data);
      }
    } catch (IOException e) {
      onFailure(new BaseHttpError(context));
    } finally {
      onFinish();
    }
  }

  /**
   * 网络请求结束
   */
  public void onFinish() {

  }

  /**
   * 失败回调
   */
  public abstract void onFailure(BaseHttpError baseHttpError);

  /**
   * 转递给子类处理
   */
  @Nullable
  public abstract T handleResponse(@NonNull Response response) throws IOException;

  /**
   * 成功回调
   */
  public abstract void onResponse(@NonNull T response);
}
