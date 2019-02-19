package com.aslan.baselibrary.listener;

import android.support.annotation.Nullable;
import com.aslan.baselibrary.http.BaseHttpError;

public interface LoadItemCallback<T> {

  void onLoaded(@Nullable T respone);

  void onDataNotAvailable(BaseHttpError error);
}
