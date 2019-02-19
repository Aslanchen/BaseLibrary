package com.aslan.baselibrary.listener;

import com.aslan.baselibrary.http.BaseHttpError;

public interface LoadCallback<T> {

  void onLoaded();

  void onDataNotAvailable(BaseHttpError error);
}
