package com.aslan.baselibrary.listener;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.aslan.baselibrary.http.BaseHttpError;

public interface LoadItemCallback<T> {

  void onLoaded(@Nullable T response);

  void onDataNotAvailable(@NonNull BaseHttpError error);
}
