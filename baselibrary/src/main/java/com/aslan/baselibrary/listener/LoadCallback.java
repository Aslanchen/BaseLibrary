package com.aslan.baselibrary.listener;

import com.aslan.baselibrary.http.BaseHttpError;

public interface LoadCallback {

  void onLoaded();

  void onDataNotAvailable(BaseHttpError error);
}
