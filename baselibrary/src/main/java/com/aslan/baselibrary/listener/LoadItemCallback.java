package com.aslan.baselibrary.listener;

import android.support.annotation.NonNull;
import com.aslan.baselibrary.http.BaseHttpError;

public interface LoadItemCallback<T> {

  void onLoaded(@NonNull T respone);

  void onDataNotAvailable(BaseHttpError error);
}
